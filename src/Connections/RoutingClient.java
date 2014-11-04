package Connections;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
    BufferedReader IN = null;

    public RoutingClient(String localIp, String adyacentNode) throws  Exception{
       adyacentIp = adyacentNode;
       socket = new Socket(adyacentIp, ROUTING_CLIENT_PORT);
       OUT = new PrintWriter(socket.getOutputStream());
       LOCAL_IP = localIp;
    }

    @Override
    public void run(){
        try {
            String helloMsg = FROM_CONSTANT + LOCAL_IP + "\n" + HELLO_CONSTANT;
            String welcomeMsg = FROM_CONSTANT + adyacentIp + "\n" + WELCOME_CONSTANT;
            OUT.println(helloMsg);
            System.out.println("MESSAGE SENT: " + helloMsg);
            OUT.flush();
            String welcome = IN.readLine();
            if (welcome.equals(welcomeMsg)){
                System.out.println("SALUDO FINALICADO CON: " + adyacentIp);
                System.out.println("ENVIAR ADYACENTES");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
