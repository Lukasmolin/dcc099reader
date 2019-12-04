package analizr.text;

import java.util.ArrayList;
import java.util.List;

import analizr.analisys.Average;
import analizr.analisys.Metric;

/**
 * StringSplitter
 */
public class StringConverter {

    public List<Metric> convertAll(List<String> text){
        List<Metric> rtn = new ArrayList<>();
        for(var s : text){
            rtn.add(convert(s));
        }
        return rtn;
    }

    public Metric convert(String text){
        String[] txt = text.split(":");
        for(int i = 0; i < txt.length; i++){
            txt[i] = txt[i].trim();
        }
        float num = -1;
        try {
            num = Float.parseFloat(txt[1].replace("'", "").replace("~", ""));
        } catch (Exception nex){
            return new Metric("ERROR::"+text, -1);
        }
        
        if(txt.length < 2){
            return new Metric("ERROR::"+text, -1);
        }
        
        return new Metric(txt[0], num);
    }

}