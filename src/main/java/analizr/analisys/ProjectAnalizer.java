package analizr.analisys;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analizr.io.TextLogger;
import analizr.io.TextReader;
import analizr.text.StringConverter;

/**
 * ProjectAnalizer
 */
public class ProjectAnalizer implements Analizer {

    private Path folder;
    private List<List<Metric>> variations = new ArrayList<>();
    private List<Metric> averagesList = new ArrayList<>();
    private List<Exception> exceptions = new ArrayList<>();

    /**
     * @return the exceptions
     */
    public List<Exception> getExceptions() {
        return exceptions;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(Path folder) {
        this.folder = folder;
    }

    public void setFolder(String folderPath) {
        Path p = FileSystems.getDefault().getPath(folderPath);
        setFolder(p);
    }

    @Override
    public void analize() throws IOException {
        if (folder == null)
            throw new IOException("Folder not setted!");

        if (!Files.isDirectory(folder))
            throw new IOException("Cannot find folder.");

        
        File[] projects = folder.toFile().listFiles();
        
        for(File project : projects){
            if(project.isDirectory()){
                var analizer = new RepoAnalizer(project.toPath().toString());
                try {
                    analizer.analize();
                } catch (Exception ex) {
                    exceptions.add(ex);
                }         
            }   
        }


        for (var project : projects) {
            if (project.isDirectory()) {
                try {
                    readVariations(project);
                } catch (Exception ex) {
                    exceptions.add(ex);
                }
            }
        }
        calculateAverage();
        averagesList.sort((first, second) -> {
            if (first.getValue() == second.getValue())
                return 0;
            if (first.getValue() < second.getValue())
                return -1;

            return +1;
        });
        persistResults();
    }

    private void readVariations(File projectDirectory) throws IOException{
        System.out.println("reading "+projectDirectory);
        var reader = new TextReader();
        
        reader.setPath(projectDirectory.getAbsolutePath() + "//variations");
        reader.readFile();
        this.variations.add(new StringConverter().convertAll(reader.getReadText()));
        for(var line : reader.getReadText()){
            System.out.println(line);
        }
    }

    private void calculateAverage() {
        System.out.println("Calculating average!");
        Map<String, Average> averages = new HashMap<>();
        for (var projectVar : variations) {
            for (var metric : projectVar) {
                if (!averages.containsKey(metric.getName()))
                    averages.put(metric.getName(), new Average());

                averages.get(metric.getName()).addToTotalSum(metric.getValue());
            }
        }
        for (var avg : averages.keySet()) {
            averagesList.add(new Metric(avg, averages.get(avg).getAverage()));
        }
        System.out.println("Average calculated");
    }

    private void persistResults() throws IOException {
        var log = new TextLogger(folder.toString(), "result");
        String txt = "";
        for (var metric : averagesList) {
            txt += "\n" + metric.getName() + ": " + metric.getValue();
        }
        log.writeLine(txt);
    }

}