package connections;

import routingtable.DistanceVector;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mario on 02/11/2014.
 */
public class RoutingClient extends  Thread{
    private static final int ROUTING_CLIENT_PORT = 9080;
    private static final String  FROM_CONSTANT = "From:";
    private static final String HELLO_CONSTANT = "Type:HELLO";
    private static final String WELCOME_CONSTANT = "Type:WELCOME";
    private static final String KA_CONSTANT = "Type:KeepAlive";
    private static final String LEN_CONSTANT = "Len:";

    private Socket socket;
    private static String LOCAL_IP = "";
    private Long TIME_CHECK_T;
    private PrintWriter OUT = null;
    private String adjacentIp;
    private Boolean successfulConnection;
    private String threadName;
    private BufferedReader IN;
    private static StateOfConnections stateOfConnections;
    private static DistanceVector distanceVector;
    private static Map<String,String> dnsTable;

    public RoutingClient(String localIp, String adjacentNode, String name, Long time, DistanceVector distanceVector,
                         Map<String,String> dnsTable){
        this.distanceVector = distanceVector;
        LOCAL_IP = localIp;
        adjacentIp = adjacentNode;
        threadName = name;
        TIME_CHECK_T = time;
        socket = new Socket();
        this.dnsTable = dnsTable;
    }

    public void verifyType(String inMsg, BufferedReader in, PrintWriter pw) throws Exception{
        String welcomeMsg = WELCOME_CONSTANT + "\n";
        String[] splitter = inMsg.split(":");
        if ((splitter[0] + ":").equals(FROM_CONSTANT )){
            if (!splitter[1].isEmpty()){
                String ipReceived  = splitter[1];
                System.out.println("<CLIENT> DEBUGGING: " + ipReceived);
                if (adjacentIp.equals(ipReceived)){
                    System.out.println("<CLIENT> DEBUGGING IP RECIVED:" + ipReceived);
                    String type = in.readLine();
                    //Revisar si es WELCOME
                    System.out.println("<CLIENT> TYPE IS:" + type);
                    if (type.equals(WELCOME_CONSTANT)){

                    }
                }
            }
        }
    }

    public void setStateOfConnectionsList(StateOfConnections stateOfConnections){
        this.stateOfConnections = stateOfConnections;
    }

    public boolean isASuccessfulConnection(){
        return successfulConnection;
    }

    public void createNewConnection(){
        try {
            System.out.println("Running a client");
            InetAddress address = InetAddress.getByName(adjacentIp);
            //socket = new Socket(address, ROUTING_CLIENT_PORT);
            socket.connect(new InetSocketAddress(address, ROUTING_CLIENT_PORT), 10000);
            OUT = new PrintWriter(socket.getOutputStream());
            IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            successfulConnection = true;
        }catch(IOException ioe){
            System.err.println("Error while try to create the new connection with the host: " + adjacentIp
                    +"\ngive the following error: " + ioe);
            this.suspend();
            successfulConnection = false;
            stateOfConnections.addNewConnection(adjacentIp,false, null);
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

    @Override
    public void run(){
        createNewConnection();
        if(successfulConnection) {
            try {
                stateOfConnections.addNewConnection(adjacentIp,true, null);
                System.out.println("Client Thread Started, ID: " + threadName);
                String helloMsg = FROM_CONSTANT + distanceVector.getLocalHostName() + "\n" + HELLO_CONSTANT;
                OUT.println(helloMsg);
                System.out.println("<CLIENT>MESSAGE SENT: " + helloMsg);
                OUT.flush();
                String welcome = IN.readLine();
                System.out.println("<CLIENT>RECIEVING FROM SERVER: " + welcome);
                verifyType(welcome, IN, OUT);
                while(stateOfConnections.getStateOfConnection(adjacentIp)){
                sleep(TIME_CHECK_T);
                    if (distanceVector.existAnyChange()) {
                        OUT.write(FROM_CONSTANT + distanceVector.getLocalHostName() + "\n");
                        OUT.flush();
                        ArrayList<String> listOfNodes = distanceVector.getDV(distanceVector.getLessDV());
                        OUT.write(LEN_CONSTANT + listOfNodes.size());
                        OUT.flush();
                        for (int i = 0; i < listOfNodes.size(); i++) {
                            System.out.println("Error msg, debbuging i:" + i);
                            String nodeDv = listOfNodes.get(i);
                            OUT.write(nodeDv + "\n");
                            System.out.println("<CLIENT> SENDING DV LIST AFTER FROM AND LEN: " + nodeDv);
                            OUT.flush();
                        }
                        distanceVector.resetFlagExistsAnyChange();
                    } else {
                        OUT.write(FROM_CONSTANT + distanceVector.getLocalHostName() + "\n" + KA_CONSTANT + "\n");
                        OUT.write(KA_CONSTANT + "\n");
                        System.out.println("<CLIENT> Sending Message: " + FROM_CONSTANT + distanceVector.getLocalHostName() + "\n"
                                + KA_CONSTANT + "\n");
                        OUT.flush();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
