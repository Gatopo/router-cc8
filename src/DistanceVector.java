import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mario on 3/11/14.
 */
public class DistanceVector extends Thread{

    private HashMap<String, Object> routingTable = null;
    private String[] element;
    private String IPSource;
    private ArrayList<String[]> node;
    private ArrayList<String> keys;

    public DistanceVector(String ip){
        routingTable = new HashMap<String, Object>();
        node = new ArrayList<String[]>();
        keys = new ArrayList<String>();
        element = null;
        IPSource = ip;
    }

    public void fillRoutingTable(ArrayList<String> configurationFile){
        element = new String[3];
        for(int i=0; i<configurationFile.size(); i++){
            for(int j=0; j<configurationFile.size(); j++){
                element[0] = getAddress(configurationFile.get(i));
                element[1] = getAddress(configurationFile.get(j));
                if(configurationFile.get(i).equals(configurationFile.get(j))) {
                    element[2] = getDistance(configurationFile.get(i));
                }else{
                    element[2] = "99";
                }
                System.out.println(element[0]);
                System.out.println(element[1]);
                System.out.println(element[2]);
                node.add(element);
            }
        }
        routingTable.put(IPSource, node);
        keys.add(IPSource);
    }

    public void addANewRoute(String newIP, ArrayList<String> configurationFile){
        element = new String[3];
        for(int i=0; i<configurationFile.size(); i++){
            for(int j=0; j<configurationFile.size(); j++){
                element[0] = getAddress(configurationFile.get(i));
                element[1] = getAddress(configurationFile.get(j));
                if(configurationFile.get(i).equals(configurationFile.get(j))) {
                    element[2] = getDistance(configurationFile.get(i));
                }else{
                    element[2] = "99";
                }
                node.add(element);
            }
        }
        routingTable.put(newIP, node);
        keys.add(newIP);
    }

    public boolean checkResendForNewElement(String IPComing, ArrayList<String> comingConnections){
        ArrayList<String[]> localTable = (ArrayList<String[]>)routingTable.get(IPSource);
        if(comingConnections != null) {
            for(int i=0; i<keys.size(); i++){
                recalculate(keys.get(i), IPComing, comingConnections);
            }
            if (routingTable.containsKey(IPComing)) {
                addANewRoute(IPComing, comingConnections);
            }
        }
        return !routingTable.containsValue(localTable);
    }



    public void recalculate(String IPkey, String IPComing, ArrayList<String> comingConnections){
        ArrayList<String[]> tempArray = (ArrayList<String[]>)routingTable.get(IPkey);
        String[] tempStringArray = null;
        for(int i=0; i<tempArray.size(); i++){
            tempStringArray = tempArray.get(i);
            if(tempStringArray[0].equals(IPComing)){
                for(int j=0; j<comingConnections.size(); j++){
                    if(tempStringArray[1].equals(getAddress(comingConnections.get(j)))){
                        tempStringArray[2] = Integer.toString(Integer.parseInt(searchForCost(tempArray, IPComing)) +
                                            Integer.parseInt(getDistance(comingConnections.get(j))));
                    }
                }
            }
        }
    }

    public String searchForCost(ArrayList<String[]> actualNode, String IPComing){
        String returnValue = "0";
        String[] tempStringArray = null;
        for(int i=0; i<actualNode.size(); i++){
            tempStringArray = actualNode.get(i);
            if(tempStringArray[0].equals(IPComing) && tempStringArray[1].equals(IPComing)){
                returnValue = tempStringArray[2];
                return returnValue;
            }
        }
        return returnValue;
    }

    public String getAddress(String pairData){
        String separatedData[];
        separatedData = pairData.split(":");
        if(separatedData != null){
            String returnData = "";
            returnData = separatedData[0].trim();
            return returnData;
        }else{
            return "";
        }
    }

    public String getDistance(String pairData){
        String separatedData[];
        separatedData = pairData.split(":");
        if(separatedData != null){
            String returnData = "";
            returnData = separatedData[1].trim();
            return returnData;
        }else{
            return "";
        }
    }

    public HashMap<String,Object> getRoutingTable(){
        return routingTable;
    }

    public String tablePrint(){
        ArrayList<String[]> row;
        String[] nodeToNodeInformation;
        String print = "";
        for(int i=0; i<keys.size(); i++){
            print = print + "Node: " + keys.get(i) + "\n";
            row = (ArrayList<String[]>)routingTable.get(keys.get(i));
            for(int j=0; j<row.size(); j++){
                nodeToNodeInformation = row.get(j);
                print = print + "from " + nodeToNodeInformation[0] +" to " + nodeToNodeInformation[1] +
                        " the cost is: " + nodeToNodeInformation[2] + "\n";
            }
        }
        return print;
    }
}
