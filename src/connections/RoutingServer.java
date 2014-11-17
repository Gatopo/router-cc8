package connections;

import routingtable.DistanceVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Mario on 02/11/2014.
 */
public class RoutingServer extends Thread {
    private static final int ROUTING_SERVER_PORT = 9080;
    private static ServerSocket serverSocket;
    private static BufferedReader IN;
    private static PrintWriter OUT_WRITER;
    private static String ADYACENT_IP;
    private Socket SOCKET;
    private static String LOCAL_IP;
    private static long TIMER_T;
    private static long TIMER_U;
    private static String ADYACENT_DNS;
    private static String ADYACENT_DISTANCE;
    private static StateOfConnections stateOfConnections;
    private static DistanceVector distanceVector;
    private static Map<String,String> dnsTable;

    public RoutingServer(String localIp, long timerT, long timerU, StateOfConnections stateOfConnections,
                         DistanceVector distanceVector, Map<String,String> dnsTable) throws Exception{
        System.out.println("Starting the server");
        serverSocket = new ServerSocket(ROUTING_SERVER_PORT);
        LOCAL_IP = localIp;
        this.distanceVector = distanceVector;
        this.TIMER_T = timerT;
        this.TIMER_U = timerU;
        this.stateOfConnections = stateOfConnections;
        this.dnsTable = dnsTable;
    }

    public void setIncomingIP(String ip, String dns, String dist){
        ADYACENT_IP = ip;
        ADYACENT_DNS = dns;
        ADYACENT_DISTANCE = dist;
    }

    public void run(){
        ReadingMessages serverReader;
        VerificationTimeTConnection validatorForAllNewClient;
        try {
            while(true) {
                SOCKET = serverSocket.accept();
                System.out.println("New connection accepted...");
                String ipNewConnection = SOCKET.getInetAddress().getHostAddress();
                System.out.println(stateOfConnections.hasConnection(ipNewConnection));
                if(stateOfConnections.hasConnection(ipNewConnection)) {
                    if (!stateOfConnections.getStateOfConnection(ipNewConnection)) {
                        RoutingClient client = new RoutingClient(LOCAL_IP, ipNewConnection,
                                "reconnect1", TIMER_T, distanceVector, dnsTable);
                        client.start();
                    }
                }
                serverReader = new ReadingMessages(SOCKET, LOCAL_IP, ipNewConnection, distanceVector);
                validatorForAllNewClient = new VerificationTimeTConnection(serverReader, TIMER_T, TIMER_U, stateOfConnections);
                serverReader.start();
                validatorForAllNewClient.start();
            }
        }catch (IOException ioe){
            System.out.println("<ERROR> Caused by:" + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}