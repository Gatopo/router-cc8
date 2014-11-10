package connections;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Mario on 02/11/2014.
 */
public class RoutingClient extends  Thread{
    private static final int ROUTING_CLIENT_PORT = 9080;
    private static final String  FROM_CONSTANT = "From:";
    private static final String HELLO_CONSTANT = "Type:HELLO";
    private static final String WELCOME_CONSTANT = "Type:WELCOME";
    private static final String KA_CONSTANT = "Type:KeepAlive";

    private Socket socket;
    private static String LOCAL_IP = "";
    private Long TIME_CHECK;
    private PrintWriter OUT = null;
    private String adyacentIp;
    private Boolean successfulConnection;
    private String threadName;
    private BufferedReader IN;

    public RoutingClient(String localIp, String adyacentNode, String name, Long time){
        adyacentIp = adyacentNode;
        TIME_CHECK = time;
        try {
            System.out.println("Starting a new client");
            InetAddress address = InetAddress.getByName(adyacentIp);
            //socket = new Socket(address, ROUTING_CLIENT_PORT);
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, ROUTING_CLIENT_PORT), 10000);
            OUT = new PrintWriter(socket.getOutputStream());
            IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            LOCAL_IP = localIp;
            successfulConnection = true;
            threadName = name;
        }catch(IOException ioe){
            System.err.println("Error while try to create the new connection with the host: " + adyacentIp
                    +"\ngive the following error: " + ioe);
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

    public void verifyType(String inMsg, BufferedReader in, PrintWriter pw) throws Exception{
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
                verifyType(welcome, IN, OUT);
                while(true){
                    sleep(TIME_CHECK);
                    OUT.write(FROM_CONSTANT + LOCAL_IP + "\n" + KA_CONSTANT + "\n");
                    OUT.write(KA_CONSTANT + "\n");
                    System.out.println("<CLIENT> Sending Message: " + FROM_CONSTANT + LOCAL_IP + "\n"
                                        + KA_CONSTANT + "\n");
                    OUT.flush();
                    //Pero tambien hay que revisar si hay cambios en la tabla.
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
