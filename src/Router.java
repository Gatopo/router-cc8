
import connections.RoutingClient;
import connections.RoutingServer;
import connections.StateOfConnections;
import routingtable.DistanceVector;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Mario on 01/11/2014.
 */

public class Router {
    private static String MY_IP;
    private static String TIME_INTERVAL;
    static Map<String,String> dnsTable;


    public static void main (String []args) throws Exception{
        /*Scanner sc = new Scanner(System.in);
        System.out.println("Enter your IP: ");
        //String myIp = sc.nextLine();
        String myIp = "192.168.1.8";
        MY_IP = myIp;
        System.out.println("MyIP: " + MY_IP);
        System.out.println("Enter Path to the file: ");
        //String filePath = sc.nextLine();
        String filePath = "/home/mario/Configuration.txt";
        System.out.println("Set the time" +
                " Interval, if empty 30's as default: ");
        String interval = sc.nextLine();
        if (interval.equals("") || interval.equals(null)){
            TIME_INTERVAL = "30";
        }else {
            TIME_INTERVAL = interval;
        }
        ArrayList<String> adyacentNodes = readFile(filePath);

        sendAdyacents(adyacentNodes);*/
        ArrayList<String> adjacentNodes = new ArrayList<String>();
        adjacentNodes.add("A:2");
        adjacentNodes.add("B:3");
        adjacentNodes.add("C:1");
        DistanceVector distanceVector = new DistanceVector(adjacentNodes, "E");
        //System.out.println(distanceVector.getRoutingTable().toString());
        //System.out.println(distanceVector.getRoutingTable().toString());
        //System.out.println(distanceVector.tablePrint());
        distanceVector.addNewNode("C", "D:1");
        distanceVector.addNewNode("A", "D:2");
        System.out.println("\n" + distanceVector.tablePrint());
        distanceVector.addNewNode("A", "D:1");
        System.out.println("\n" + distanceVector.tablePrint());
        System.out.println("\n" + distanceVector.getDV(distanceVector.getLessDV()));
    }

    private static ArrayList readFile (String path) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> nodes = new ArrayList<String>();
        if (reader != null) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                nodes.add(line);
            }
        }
        return nodes;
    }

    private static void sendAdyacents (ArrayList<String> nodes) throws Exception{
        Integer realTime = Integer.parseInt(TIME_INTERVAL) * 1000;
        Long longTime = new Long(realTime.toString());
        StateOfConnections stateOfConnections = new StateOfConnections();
        RoutingServer rs = new RoutingServer(MY_IP, realTime, 90000, stateOfConnections);
        RoutingClient rc;
        rs.start();
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).contains(":")) {
                String[] firstNode = nodes.get(i).split(":");
                String dns = firstNode[0];
                String ip = firstNode[1];
                String distance = firstNode[2];
                rs.setIncomingIP(ip, dns, distance);
                //System.out.println("Client Thread Started, ID: " + i);
                Integer name = i;
                rc = new RoutingClient(MY_IP, ip, name.toString(), longTime);
                rc.setStateOfConnectionsList(stateOfConnections);
                rc.start();
            }
        }
    }


}
