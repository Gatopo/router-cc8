
import Connections.RoutingClient;
import Connections.RoutingServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mario on 01/11/2014.
 */
public class Router {
    private static String MY_IP;
    public static void main (String []args) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your IP: ");
        String myIp = sc.nextLine();
        System.out.println("Enter Path to the file: ");
        String filePath = sc.nextLine();
        ArrayList<String> adyacentNodes = readFile(filePath);
        //sendAdyacents(adyacentNodes);
        DistanceVector distanceVector = new DistanceVector(myIp);
        distanceVector.fillRoutingTable(adyacentNodes);
        System.out.println(distanceVector.getRoutingTable().toString());
        ArrayList<String> newRow = new ArrayList<String>();
        newRow.add("555:4");
        newRow.add("222:9");
        distanceVector.checkResendForNewElement("456",newRow);
        System.out.println(distanceVector.tablePrint());
        /*
        ArrayList<String[]> tempArray = (ArrayList<String[]>)distanceVector.getRoutingTable().get("1234");
        String[] tempStringArray = tempArray.get(0);
        System.out.println(tempStringArray[0]);
        System.out.println(tempStringArray[1]);
        System.out.println(tempStringArray[2]);
        */
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
        for (int i = 0; i <= nodes.size(); i++){
            if (nodes.get(i).contains(";")) {
                String[] firstNode = nodes.get(i).split(";");
                String ip = firstNode[0];
                RoutingClient rc = new RoutingClient(MY_IP, ip);
                RoutingServer rs = new RoutingServer(ip);
                rc.start();
                System.out.println("Client Thread Started, ID: " + i);
                rs.start();
                System.out.println("Server Thread Started, ID: " + i);
            }
        }
    }


}
