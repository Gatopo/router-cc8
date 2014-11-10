package routingtable;

/**
 * Created by Steven on 06/11/2014.
 */
public class TableElement {
    private String nodeDestiny;
    private String nodeAdjacent;
    private int cost;

    public TableElement(String adjacent, String destiny, int routCost){
        this.nodeAdjacent = adjacent;
        this.nodeDestiny = destiny;
        this.cost = routCost;
    }

    public String getNodeDestiny(){
        return this.nodeDestiny;
    }

    public String getNodeAdjacent(){
        return this.nodeAdjacent;
    }

    public int getRouteCost(){
        return this.cost;
    }
}
