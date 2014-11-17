package forwarding;

import routingtable.DistanceVector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Steven on 17/18/2014.
 */
public class ForwardingServer extends Thread {
    private static final int FORWARDING_SERVER_PORT = 1981;
    private static ServerSocket serverSocket;
    private Socket SOCKET;
    private static String LOCAL_IP;
    private static Map<String, String> DNSTable;
    private static DistanceVector distanceVector;

    public ForwardingServer(String localIp, Map<String, String> DNSTable, DistanceVector routingTable) throws Exception {
        System.out.println("Starting the forwarding server");
        serverSocket = new ServerSocket(FORWARDING_SERVER_PORT);
        LOCAL_IP = localIp;
        this.distanceVector = routingTable;
        this.DNSTable = DNSTable;
    }

    public void run(){
        try {
            while(true) {
                SOCKET = serverSocket.accept();
                System.out.println("New connection...");
                String ipNewConnection = SOCKET.getInetAddress().getHostAddress();
                if(DNSTable.containsKey(ipNewConnection)){
                    System.out.println("Connection accepted");
                    ReadingMessages readingMessages = new ReadingMessages(SOCKET, LOCAL_IP, ipNewConnection, DNSTable,
                            distanceVector);
                }else{
                    System.out.println("Connection rejected");
                }
            }
        }catch (IOException ioe){
            System.out.println("<ERROR> Caused by:" + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}