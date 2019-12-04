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
import analizr.text.TextSplitter;

/**
 * Analizer
 */
public class UnitAnalizer implements Analizer{

    private Path folder;
    private List<String> analizedFiles = new ArrayList<>();
    private List<List<Metric>> results = new ArrayList<>();;
    private Map<String, Average> averages = new HashMap<>();

    public void setFolderPath(Path path) {
        this.folder = path;
    }

    public void setFolderPath(String path) {
        Path p = FileSystems.getDefault().getPath(path);
        this.setFolderPath(p);
    }

    @Override
    public void analize() throws IOException {
        listFileNames();
        readMetrics();
        calculateAverages();
        persistResult();
    }

    private void listFileNames() throws IOException {
        if(folder == null)
            throw new NullPointerException("Path to folder is null! Have you setted it correctly?");

        if(!Files.isDirectory(folder))
            throw new IOException("Folder path is not a directory!");

        analizedFiles.clear();
        File[] listOfFiles = folder.toFile().listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile())
                analizedFiles.add(listOfFiles[i].getName());
        }
    }

    private void readMetrics() throws IOException {
        TextReader reader;
        for(var file : analizedFiles){
            reader = new TextReader(folder.toString() + "//" + file);
            reader.readFile();
            var txtList = new TextSplitter().process(reader.getReadText());
            results.add(new StringConverter().convertAll(txtList));
        }
    }

    private void calculateAverages(){
        for(var metrics : results){
            for(var metric : metrics){
                if(!averages.containsKey(metric.getName()))
                    averages.put(metric.getName(), new Average());

                averages.get(metric.getName()).addToTotalSum(metric.getValue());
            }
        }
    }

    private void persistResult() throws IOException {
        var log = new TextLogger(folder.toString(), "averages");
        String txtLog = "";
        for(var metric : averages.keySet()){
            txtLog += "\n"+metric+": "+averages.get(metric).getAverage();
        }

        log.writeLine(txtLog);
    }
}