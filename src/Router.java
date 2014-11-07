
import connections.RoutingClient;
import connections.RoutingServer;
import routingtable.DistanceVector;

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
        MY_IP = myIp;
        System.out.println("MyIP: " + MY_IP);
        System.out.println("Enter Path to the file: ");
        String filePath = sc.nextLine();
        ArrayList<String> adyacentNodes = readFile(filePath);
        //sendAdyacents(adyacentNodes);
        DistanceVector distanceVector = new DistanceVector(myIp, adyacentNodes);
        //System.out.println(distanceVector.getRoutingTable().toString());
        System.out.println(distanceVector.tablePrint());
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
        RoutingServer rs = new RoutingServer();
        rs.start();
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).contains(":")) {
                String[] firstNode = nodes.get(i).split(":");
                String ip = firstNode[0];
                rs.setIncomingIP(ip);
                System.out.println("Server Thread Started, ID: " + i);
                RoutingClient rc = new RoutingClient(MY_IP, ip);
                rc.start();
                System.out.println("Client Thread Started, ID: " + i);

            }
        }
    }


}
