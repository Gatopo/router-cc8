package routingtable;

/**
 * Created by Steven on 06/11/2014.
 */
public class TableElement {
    private String ipAdyacent;
    private int cost;

    public TableElement(String IPAdyacent, int routCost){
        this.ipAdyacent = IPAdyacent;
        this.cost = routCost;
    }

    public String getIpAdyacent(){
        return this.ipAdyacent;
    }

    public int getRouteCost(){
        return this.cost;
    }
}
