package Connections;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Mario on 02/11/2014.
 */
public class RoutingClient extends  Thread{
    private static final int ROUTING_CLIENT_PORT = 9080;
    private static final String  FROM_CONSTANT = "From:";
    private static final String HELLO_CONSTANT = "Type:HELLO";
    private static final String WELCOME_CONSTANT = "Type:WELCOME";

    private Socket socket;
    private static String LOCAL_IP = "";
    private PrintWriter OUT = null;
    private String adyacentIp;
    private Boolean successfulConnection;
    private String threadName;
    BufferedReader IN;

    public RoutingClient(String localIp, String adyacentNode, String name){
        adyacentIp = adyacentNode;
        try {
            InetAddress address = InetAddress.getByName(adyacentIp);
            socket = new Socket(address, ROUTING_CLIENT_PORT);
            OUT = new PrintWriter(socket.getOutputStream());
            IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            LOCAL_IP = localIp;
            successfulConnection = true;
            threadName = name;
        }catch(IOException ioe){
            System.err.println("Error while try to create the new connection: " + ioe);
            successfulConnection = false;
        }catch(SecurityException se){
            System.err.println("A security method don´t allow the connection: " + se);
            successfulConnection = false;
        }catch(IllegalArgumentException iae){
            System.err.println("Error, the port inserted don´t exist: " + iae);
            successfulConnection = false;
        }catch(NullPointerException npe){
            System.err.println("Error, the given host is empty: " + npe);
            successfulConnection = false;
        }
    }

    public void verifyType(String inMsg, BufferedReader in) throws Exception{
        String welcomeMsg = WELCOME_CONSTANT + "\n";
        String[] splitter = inMsg.split(":");
        if ((splitter[0] + ":").equals(FROM_CONSTANT )){
            if (!splitter[1].isEmpty()){
                String ipReceived  = splitter[1];
                System.out.println("<CLIENT> DEBUGGING: " + ipReceived);
                if (adyacentIp.equals(ipReceived)){
                    System.out.println("<CLIENT> DEBUGGING IP RECIVED:" + ipReceived);
                    String type = in.readLine();
                    //Revisar si es WELCOME
                    System.out.println("<CLIENT> TYPE IS:" + type);
                    if (type.equals(WELCOME_CONSTANT)){
                        System.out.println("<CLIENT>\n Starting to send DV");
                    }
                }
            }
        }
    }

    public boolean isASuccessfulConnection(){
        return successfulConnection;
    }

    @Override
    public void run(){
        if(successfulConnection) {
            try {
                System.out.println("Client Thread Started, ID: " + threadName);
                String helloMsg = FROM_CONSTANT + LOCAL_IP + "\n" + HELLO_CONSTANT;
                OUT.println(helloMsg);
                System.out.println("<CLIENT>MESSAGE SENT: " + helloMsg);
                OUT.flush();
                String welcome = IN.readLine();
                System.out.println("<CLIENT>RECIEVING FROM SERVER: " + welcome);
                verifyType(welcome, IN);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
