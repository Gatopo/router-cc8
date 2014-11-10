package routingtable;

import java.util.*;

/**
 * Created by Steven on 3/11/14.
 */
public class DistanceVector extends Thread{

    private HashMap<String, ArrayList<TableElement>> routingTable;
    private ArrayList<TableElement> elements;

    public DistanceVector(ArrayList<String> configFile){
        routingTable = new HashMap<String, ArrayList<TableElement>>();
        //tableNode = new HashMap<String, ArrayList<TableElement>>();
        //fields = new ArrayList<TableElement>();
        fillRoutingTable(configFile);
    }

    public void addNewNode( ){

    }

    public void addANewRoute(String newIP, ArrayList<String> configurationFile){

    }

    public void fillRoutingTable(ArrayList<String> comingConnections){
        TableElement tableElement;
        ArrayList<TableElement> elements = new ArrayList<TableElement>();
        String piecesNodeI[];
        String piecesNodeJ[];
        String outNode;
        int cost;
        int listSize = comingConnections.size();
        for(int i=0; i<listSize; i++){
            elements.clear();
            outNode = comingConnections.get(i);
            System.out.println("******************* outing: "+outNode);
            piecesNodeI = comingConnections.get(i).split(":");
            comingConnections.remove(i);
            for(int j=0; j<listSize-1; j++) {
                piecesNodeJ = comingConnections.get(j).split(":");
                System.out.println("******************* getting: "+comingConnections.get(j));
                System.out.println("******************* part: "+piecesNodeI[0]);
                System.out.println("******************* part: "+piecesNodeJ[0]);
                if(piecesNodeI[0].equals(piecesNodeJ[0])){
                    cost = Integer.parseInt(piecesNodeI[1]);
                }else{
                    cost = 99;
                }
                tableElement = new TableElement(piecesNodeJ[0], piecesNodeI[0], cost);
                elements.add(tableElement);
            }
            piecesNodeI = outNode.split(":");
            routingTable.put(piecesNodeI[0], elements);
            comingConnections.add(i,outNode);
            System.out.println("******************* inserting: "+outNode);
        }
    }

    public HashMap<String,ArrayList<TableElement>> getRoutingTable(){
        return routingTable;
    }

    public String tablePrint(){
        String tablePrint = "Routing Table\n" +
                "-------------------------------------";
        Set<String> keysTable = routingTable.keySet();
        Iterator<String> iteratorKeys = keysTable.iterator();
        ArrayList<TableElement> elements;
        TableElement element;
        String tempKeyTable = "";
        String nodePrint = "";
        while(iteratorKeys.hasNext()) {
            tempKeyTable = iteratorKeys.next();
            nodePrint += "\n" + tempKeyTable + "\n";
            elements = routingTable.get(tempKeyTable);
            for(int i=0; i<elements.size(); i++){
                element = elements.get(i);
                nodePrint += "Adjacent: " + element.getNodeAdjacent()
                            + " Destiny: " + element.getNodeDestiny()
                            + " Cost:" + element.getRouteCost()
                            + "\n";
            }
        }
        tablePrint += nodePrint;
        /*
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
        */
        return tablePrint;
    }
}
