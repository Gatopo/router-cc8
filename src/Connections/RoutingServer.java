package connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
    private static long timer;
    private static String ADYACENT_DNS;
    private static String ADYACENT_DISTANCE;

    public RoutingServer(String localIp, long timer) throws Exception{
        System.out.println("Starting the server");
        serverSocket = new ServerSocket(ROUTING_SERVER_PORT);
        LOCAL_IP = localIp;
        this.timer = timer;
    }

    public void setIncomingIP(String ip, String dns, String dist){
        ADYACENT_IP = ip;
        ADYACENT_DNS = dns;
        ADYACENT_DISTANCE = dist;
    }

    public void run(){
        ReadingMessages serverReader;
        VerificationTimeUConnection validatorForAllNewClient;
        try {
            while(true) {
                SOCKET = serverSocket.accept();
                System.out.println("New connection accepted...");
                serverReader = new ReadingMessages(SOCKET, LOCAL_IP);
                validatorForAllNewClient = new VerificationTimeUConnection(serverReader, timer);
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