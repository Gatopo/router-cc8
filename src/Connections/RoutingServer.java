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
    private static Socket SOCKET;
    private static String LOCAL_IP;

    public RoutingServer(String localIp) throws Exception{
        System.out.println("Starting a the server");
        serverSocket = new ServerSocket(ROUTING_SERVER_PORT);
        LOCAL_IP = localIp;
    }

    public void setIncomingIP(String ip){
        ADYACENT_IP = ip;
    }

    public void run(){
        ReadingMessages serverReder
        try {
            while(true) {
                SOCKET = serverSocket.accept();
                System.out.println("New connection accepted...");
                serverReder = new ReadingMessages(SOCKET, LOCAL_IP);
                serverReder.start();
            }
        }catch (IOException ioe){
            System.out.println("<ERROR> Cuased by:" + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}