package analizr.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TextReader
 */
public class TextReader {

    private Path path;
    private List<String> fileText = new ArrayList<>();

    public TextReader() { }

    public TextReader(Path filePath){
        if(Files.isReadable(filePath)){
            this.path = filePath;
        }
    }

    public TextReader(String filePath){        
        this(FileSystems.getDefault().getPath(filePath));
    }

    /**
     * Returns the path in String form
     * @return the path in string form if setted, empty string otherwise
     */
    public String getPath(){
        if(path != null){
            return path.toString();
        }
        return "";
    }

    /**
     * Convenience method for setting path as String, uses setPath(path)
     * @param path desired path to read
     * @return true if able to set path, false otherwise
     */
    public boolean setPath(String path){
        Path p = FileSystems.getDefault().getPath(path);
        return setPath(p);
    }

    /**
     * Sets path if Files.isReadable is true
     * @param path desired path ro read
     * @return true if path was succesfully setted, false otherwise
     */
    public boolean setPath(Path path){
        if(Files.isReadable(path)){
            this.path = path;
            return true;
        }
        System.out.println("Path not readable set attempt");
        return false;
    }

    /**
     * Cheks if the setted path is still readable
     * @return true if the setted path is readable, false otherwise
     */
    public boolean isPathReadable(){
        if(this.path == null)
            return false;
        
        if(Files.isReadable(this.path))
            return true;

        return false;
    }

    /**
     * Tries to read information from the desired path
     * @throws IOException if this.isPathReadable() evaluates to false
     */
    public void readFile() throws IOException{
        if(!isPathReadable())
            throw new IOException("this.isReadable() is FALSE!. Not able to read file.");

        Scanner scanner = new Scanner(this.path);
        this.fileText.clear();
        while(scanner.hasNextLine()){
            this.fileText.add(scanner.nextLine());
        }
        scanner.close();
    }

    /**
     * Method to retrieve the last read file
     * @return last read file or empty if no file has been read yet
     */
    public List<String> getReadText(){
        return this.fileText;
    }

    /**
     * Returns last read file as a String
     * @return las read file or empty string if no file has been read yet
     */
    public String getReadAsString(){
        String s = "";
        for(var line : this.fileText){
            s += "\n"+line;
        }
        return s;
    }


    
}