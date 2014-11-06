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
    BufferedReader IN;

    public RoutingClient(String localIp, String adyacentNode) throws  Exception{
       adyacentIp = adyacentNode;
       InetAddress address = InetAddress.getByName(adyacentIp);
       socket = new Socket(address, ROUTING_CLIENT_PORT);
       OUT = new PrintWriter(socket.getOutputStream());
       IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       LOCAL_IP = localIp;
    }

    public void verifyType(String inMsg, BufferedReader in) throws Exception{
        String welcomeMsg = WELCOME_CONSTANT + "\n";
        String[] splitter = inMsg.split(":");
        if ((splitter[0] + ":").equals(FROM_CONSTANT )){
            if (!splitter[1].isEmpty()){
                String ipReceived  = splitter[1];
                if (adyacentIp.equals(ipReceived)){
                    System.out.println("<CLIENT> DEBUGGING IP RECIVED:" + ipReceived);
                    String type = in.readLine();
                    //Revisar si es WELCOME
                    System.out.println("<CLIENT> TYPE IS:" + type);
                    if (type.equals(WELCOME_CONSTANT)){
                        System.out.println("<CLIENT> Starting to send DV");
                    }
                }
            }
        }
    }


    @Override
    public void run(){
        try {
            String helloMsg = FROM_CONSTANT + LOCAL_IP + "\n" + HELLO_CONSTANT;
            OUT.println(helloMsg);
            System.out.println("<CLIENT>MESSAGE SENT: " + helloMsg);
            OUT.flush();
            String welcome = IN.readLine();
            System.out.println("<CLIENT>RECIEVING FROM SERVER: " + welcome);
            verifyType(welcome, IN);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
