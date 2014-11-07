package routingtable;

import java.util.*;

/**
 * Created by Steven on 3/11/14.
 */
public class DistanceVector extends Thread{

    private HashMap<String, Object> routingTable;
    private HashMap<String, ArrayList<TableElement>> tableNode;
    private ArrayList<TableElement> fields;
    private String myIp;

    public DistanceVector(String myIP, ArrayList<String> comingConnections){
        this.myIp = myIP;
        routingTable = new HashMap<String, Object>();
        tableNode = new HashMap<String, ArrayList<TableElement>>();
        fields = new ArrayList<TableElement>();
        fillRoutingTable(comingConnections);
    }

    public void addNewNode( ){

    }

    public void addANewRoute(String newIP, ArrayList<String> configurationFile){

    }

    public void fillRoutingTable(ArrayList<String> comingConnections){
        String piecesNode[];
        TableElement tableElement;
        String node;
        int cost;
        for(int i=0; i<comingConnections.size(); i++){
            fields.clear();
            tableNode.clear();
            for(int j=0; j<comingConnections.size(); j++) {
                node = comingConnections.get(j);
                piecesNode = node.split(":");
                if(i==j) {
                    cost = Integer.parseInt(piecesNode[1]);
                }else{
                    cost = 99;
                }
                tableElement = new TableElement(piecesNode[0], cost);
                fields.add(tableElement);
                tableNode.put(piecesNode[0], fields);
            }
            node = comingConnections.get(i);
            piecesNode = node.split(":");
            routingTable.put(piecesNode[0], tableNode);
        }
    }

    public HashMap<String,Object> getRoutingTable(){
        return routingTable;
    }

    public String tablePrint(){
        Set<String> keysTable = routingTable.keySet();
        Set<String> keysNode;
        Iterator<String> iteratorKeys = keysTable.iterator();
        Iterator<String> iteratorNode;
        ArrayList<TableElement> element;
        TableElement tableElement;
        String tablePrint = "Routing Table\n" +
                            "-------------------------------------";
        String tempKeyTable = "";
        String tempKeyNode = "";
        HashMap<String, ArrayList<TableElement>> tableNode;
        while(iteratorKeys.hasNext()){
            tempKeyTable = iteratorKeys.next();
            tableNode = (HashMap<String, ArrayList<TableElement>>)routingTable.get(tempKeyTable);
            keysNode = tableNode.keySet();
            iteratorNode = keysNode.iterator();
            int cont = 0;
            String nodePrint = "Node: " + tempKeyTable + "\n";
            while(iteratorNode.hasNext()){
                tempKeyNode = iteratorNode.next();
                element = tableNode.get(tempKeyNode);
                for(int i=0; i<element.size(); i++) {
                    tableElement = element.get(i);
                    nodePrint += "From: " + tableElement.getIpAdyacent() + " to " + tempKeyNode + " cost:" + tableElement.getRouteCost() + "\n";
                }
            }
            tablePrint += "\n" + nodePrint + "\n";
        }
        return tablePrint;
    }
}
