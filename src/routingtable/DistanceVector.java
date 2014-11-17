package routingtable;

import javafx.scene.control.Tab;

import java.util.*;

/**
 * Created by Steven on 3/11/14.
 */
public class DistanceVector extends Thread{

    private HashMap<String, ArrayList<TableElement>> routingTable;
    private ArrayList<TableElement> elements;
    private String localHostName;
    private boolean existsAnyChange;
    private ArrayList<String> destinyNodes;

    public DistanceVector(ArrayList<String> configFile, String localHostName){
        routingTable = new HashMap<String, ArrayList<TableElement>>();
        destinyNodes = new ArrayList<String>();
        this.localHostName = localHostName;
        fillRoutingTable(configFile);
        this.existsAnyChange = true;
    }

    public void addNewNode(String fromNode, String dv){
        String[] piecesDV = dv.split(":");
        if(!containsElement(fromNode, piecesDV[0])){
            containsDestiny(piecesDV[0]);
            ArrayList<TableElement> elements = routingTable.get(localHostName);
            int cost = Integer.parseInt(piecesDV[1]);
            cost += getCostDV(fromNode);
            TableElement tableElement = new TableElement(fromNode, piecesDV[0], cost);
            elements.add(tableElement);
            //order();
            routingTable.put(localHostName, elements);
        }else{
            List<TableElement> elements = routingTable.get(localHostName);
            TableElement element;
            int sizeCompare = elements.size();
            for(int i=0; i<sizeCompare; i++){
                element = elements.get(i);
                if(element.getNodeAdjacent().equals(fromNode)){
                    if(element.getNodeDestiny().equals(piecesDV[0])){
                        int cost = Integer.parseInt(piecesDV[1]);
                        cost += getCostDV(fromNode);
                        element.setNewRouteCost(cost);
                    }
                }
            }
        }
        existsAnyChange = true;
    }

    public boolean containsElement(String node, String destiny){
        List<TableElement> elements = routingTable.get(localHostName);
        TableElement element;
        String tempDestiny;
        for(int i=0; i<elements.size(); i++){
            element = elements.get(i);
            tempDestiny = element.getNodeDestiny();
            if(destiny.equals(tempDestiny) && node.equals(element.getNodeAdjacent())){
                return true;
            }
        }
        return false;
    }

    public void fillRoutingTable(ArrayList<String> comingConnections){
        ArrayList<TableElement> elements = new ArrayList<TableElement>();
        TableElement tableElement;
        int tableSize = comingConnections.size();
        String[] pieceAdjacent;
        String[] pieceDestiny;
        String adjacent;
        String destiny;
        int cost = 0;
        for(int i=0; i<tableSize; i++){
            pieceAdjacent = comingConnections.get(i).split(":");
            adjacent = pieceAdjacent[0];
            destinyNodes.add(adjacent);
            for(int j=0; j<tableSize; j++){
                pieceDestiny = comingConnections.get(j).split(":");
                destiny = pieceDestiny[0];
                if(destiny.equals(adjacent)){
                    cost = Integer.parseInt(pieceAdjacent[1]);
                }else{
                    cost = 99;
                }
                tableElement = new TableElement(adjacent, destiny, cost);
                elements.add(tableElement);
            }
        }
        routingTable.put(localHostName, elements);
    }

    public void order(){
        List<TableElement> elements = routingTable.get(localHostName);
        TableElement element;
        TableElement compareElement;
        TableElement auxTableElement;
        int sizeCompare = elements.size()-1;
        for(int i=0; i<sizeCompare; i++){
            element = elements.get(i);
            for(int j=i+1; j<sizeCompare; j++) {
                compareElement = elements.get(j);
                if(element.isBiggerThan(compareElement)){
                    System.out.println("is bigger");
                    auxTableElement = compareElement;
                    compareElement = element;
                    element = auxTableElement;
                    elements.remove(i);
                    elements.remove(j);
                    elements.add(i, element);
                    elements.add(j,auxTableElement);
                }
            }
        }
    }

    public ArrayList<String> getDV(ArrayList<TableElement> lessDV){
        ArrayList<String> dv = new ArrayList<String>();
        TableElement element;
        int sizeCompare = lessDV.size();
        for(int i=0; i<sizeCompare; i++){
            dv.add(lessDV.get(i).stringDV());
        }
        return dv;
    }

    public ArrayList<TableElement> getLessDV(){
        ArrayList<TableElement> lessDistanceVectors = new ArrayList<TableElement>();
        List<TableElement> elements = routingTable.get(localHostName);
        TableElement element;
        TableElement lessTableElement;
        String destinyNode;
        for(int i=0; i<destinyNodes.size(); i++){
            destinyNode = destinyNodes.get(i);
            lessTableElement = new TableElement("",destinyNode,99);
            for(int j=0; j<elements.size(); j++){
                element = elements.get(j);
                if(element.getNodeDestiny().equals(destinyNode)){
                    if(element.isCostLessThan(lessTableElement)) {
                        lessTableElement = element;
                    }
                }
            }
            lessDistanceVectors.add(lessTableElement);
        }
        return lessDistanceVectors;
    }

    public void containsDestiny(String destiny){
        List<TableElement> elements = routingTable.get(localHostName);
        TableElement element;
        for(int i=0; i<elements.size(); i++){
            element = elements.get(i);
            if(element.getNodeDestiny().equals(destiny)){
                return;
            }
        }
        destinyNodes.add(destiny);
    }


    public HashMap<String,ArrayList<TableElement>> getRoutingTable(){
        return routingTable;
    }

    public void resetFlagExistsAnyChange(){
        this.existsAnyChange = false;
    }

    public int getCostDV(String node){
        List<TableElement> elements = routingTable.get(localHostName);
        TableElement element;
        int sizeCompare = elements.size();
        int cost = 99;
        for(int i=0; i<sizeCompare; i++){
            element = elements.get(i);
            if(element.getNodeAdjacent().equals(node)){
                if(element.getNodeDestiny().equals(node)){
                    cost = element.getRouteCost();
                    return cost;
                }
            }
        }
        return cost;
    }

    public String tablePrint(){
        String tablePrint = "Routing Table\n" +
                "-------------------------------------";
        String nodePrint = "";
        List<TableElement> connections = routingTable.get(localHostName);
        for(int i=0; i<connections.size(); i++){
            nodePrint += "\n" + connections.get(i);
        }
        tablePrint += nodePrint;
        return tablePrint;
    }
}
