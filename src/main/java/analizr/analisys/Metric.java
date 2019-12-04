package analizr.analisys;

/**
 * Metric
 */
public class Metric {

    private String name;
    private float value;

    public Metric(String name, float value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return "|"+this.name+"||"+this.value+"|";
    }

}