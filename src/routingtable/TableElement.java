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

    public void setNewRouteCost(int newCost){
        this.cost = newCost;
    }

    public boolean isBiggerThan(TableElement otherNode){
        char first = this.getNodeAdjacent().charAt(0);
        char second = otherNode.getNodeAdjacent().charAt(0);
        return first > second;
    }

    public boolean isCostLessThan(TableElement otherNode){
        return this.getRouteCost() < otherNode.getRouteCost();
    }

    public String toString() {
        String tableElementRepresentation = "";
        tableElementRepresentation += "Adjacent: " + nodeAdjacent + " Destiny: " + nodeDestiny + " Cost: " + cost;
        return tableElementRepresentation;
    }

    public String stringDV(){
        return nodeDestiny + ":" + Integer.toString(cost);
    }
}
