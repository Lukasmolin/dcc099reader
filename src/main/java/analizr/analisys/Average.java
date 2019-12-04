package analizr.analisys;

/**
 * Average
 */
public class Average {

    private int counted = 0;
    private float totalSum = 0;

    public int getCounted() {
        return counted;
    }

    public void setCounted(int counted) {
        if(counted >= 0){
            this.counted = counted;
        }
    }

    public float getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(float totalSum) {
        this.totalSum = totalSum;
    }

    public void addToTotalSum(float toAdd){
        if(toAdd >= 0){
            this.totalSum += toAdd;
            this.counted++;
        }
    }

    public float getAverage(){
        if(this.counted == 0)
            return -1;

        return this.totalSum / this.counted;
    }

    @Override
    public String toString(){
        return this.totalSum +"/"+ this.counted;
    }

}