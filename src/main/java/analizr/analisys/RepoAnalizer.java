package analizr.analisys;

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
 * RepoAnalizer
 */
public class RepoAnalizer implements Analizer {

    private Path folder;
    List<Metric> control = new ArrayList<>();
    List<Metric> bug = new ArrayList<>();
    Map<String, Float> variations = new HashMap<>();

    public RepoAnalizer(String folderPath) {
        setPath(folderPath);
    }

    public void setPath(String path) {
        Path p = FileSystems.getDefault().getPath(path);
        this.folder = p;
    }

    @Override
    public void analize() throws IOException {
        Path bug = FileSystems.getDefault().getPath(folder.toString(), "bug");
        Path control = FileSystems.getDefault().getPath(folder.toString(), "control");

        if(!Files.isDirectory(bug))
            throw new IOException("bug folder not found for "+folder);

        if(!Files.isDirectory(control))
            throw new IOException("control folder not found!");

        var controlAnalizer = new UnitAnalizer();
        var bugAnalizer = new UnitAnalizer();
        controlAnalizer.setFolderPath(control);
        bugAnalizer.setFolderPath(bug);
        controlAnalizer.analize();
        bugAnalizer.analize();


        readAnalisys(bug, control);
        calculateVariation();
        persistAverages();
    }

    private void readAnalisys(Path bug, Path control) throws IOException{
        var controlReader = new TextReader(control.toString()+"//averages");
        var bugReader = new TextReader(bug.toString()+"//averages");
        controlReader.readFile();
        bugReader.readFile();

        List<String> controlMetrics = controlReader.getReadText();
        List<String> bugMetrics = bugReader.getReadText();

        this.control = new StringConverter().convertAll(controlMetrics);
        this.bug = new StringConverter().convertAll(bugMetrics);
    }

    private void calculateVariation(){
        variations.clear();
        for(var metric : this.control){
            variations.put(metric.getName(), metric.getValue());
        }
        for(var metric : this.bug){
            if(variations.containsKey(metric.getName())){
                double ctrMetric = variations.get(metric.getName());
                if(ctrMetric == 0)
                    ctrMetric = 0.000000000000001;

                float variation = (float)(metric.getValue()*100/ctrMetric);
                variations.put(metric.getName(), variation);
            }
        }
    }

    private void persistAverages() throws IOException{
        var varLog = new TextLogger(folder.toString(), "variations");
        String txt = "";        
        for(var variation : variations.keySet()){
            txt += "\n"+variation+": "+variations.get(variation).toString();
        }
        varLog.writeLine(txt);
    }





    //Calcula as metricas interiores
    //calcula a metrica geral

}