package analizr.text;

import java.util.List;

/**
 * TextConverter
 */
public class TextSplitter {
    private List<String> text;

    /**
     * Splits the analizo text
     * @param text
     * @return a processed list
     */
    public List<String> process(List<String> text) {
        
        int start = -1, end = -1;
        for(int i = 0; i < text.size() && end == -1; i++){
            if(text.get(i).equals("---")){
                if(start == -1)
                    start = i;
                else
                    end = i;
            }
        }

        if(start == -1 || start+1 >= text.size()){
            this.text = text;
        } else if (end == -1){
            this.text = text.subList(start+1, text.size()-1);
        } else {
            this.text = text.subList(start+1, end);
        }

        return this.text;
    }

    public List<String> getText(){
        return this.text;
    }

}