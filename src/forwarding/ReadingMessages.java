package forwarding;

import routingtable.DistanceVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Steven on 17/11/2014.
 */
public class ReadingMessages {

    private static final String FROM_CONSTANT = "From";
    private static final String TO_CONSTANT = "To";
    private static final String MSG_CONSTANT = "Msg";
    private static final String EOF_CONSTANT = "EOF";
    private static final int FORWARDING_SERVER_PORT = 1981;


    private String fromMessage;
    private String toMessage;
    private String message;
    private BufferedReader IN;
    private PrintWriter OUT;
    private static String MY_IP;
    private String adjacentHostIP;
    private boolean newMessage;
    private DistanceVector distanceVector;
    private Map<String,String> dnsTable;


    public ReadingMessages(Socket serverSocket, String localIp, String adjacentIP, Map<String,String> dnsTable,
                           DistanceVector routingTable) throws Exception{
        IN = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        OUT = new PrintWriter(serverSocket.getOutputStream());
        MY_IP =localIp;
        newMessage = false;
        this.adjacentHostIP = adjacentIP;
        this.distanceVector = routingTable;
    }

    public boolean isFromMessage(String compare){
        String[] piecesMsg = compare.split(":");
        if(!piecesMsg[1].isEmpty() && piecesMsg[0].equals(FROM_CONSTANT)){
            fromMessage = piecesMsg[1];
            return true;
        }else{
            return false;
        }
    }

    public boolean isToMessage(String compare){
        String[] piecesMsg = compare.split(":");
        if(!piecesMsg[1].isEmpty() && piecesMsg[0].equals(TO_CONSTANT)){
            toMessage = piecesMsg[1];
            return true;
        }else{
            return false;
        }
    }

    public boolean isMsg(String compare){
        String[] piecesMsg = compare.split(":");
        if(!piecesMsg[1].isEmpty() && piecesMsg[0].equals(MSG_CONSTANT)){
            message = piecesMsg[1];
            return true;
        }else{
            return false;
        }
    }

    public boolean isEOFMessage(String compare){
        String[] piecesMsg = compare.split(":");
        if(!piecesMsg[0].equals(EOF_CONSTANT)){
            message += "\n" + piecesMsg[1];
            return true;
        }else{
            return false;
        }
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void run(){
        try {
            String clientMsg;
            clientMsg = IN.readLine();
            if(isFromMessage(clientMsg)){
                clientMsg = IN.readLine();
                if(isToMessage(clientMsg)){
                    clientMsg = IN.readLine();
                    if(isMsg(clientMsg)){
                        clientMsg = IN.readLine();
                        int cont = 0;
                        while(isEOFMessage(clientMsg) && cont<25){
                            clientMsg = IN.readLine();
                        }
                        System.out.println("<FORWARDING>");
                        System.out.println(FROM_CONSTANT + ":" + fromMessage);
                        System.out.println(TO_CONSTANT + ":" + toMessage);
                        if(!distanceVector.getLocalHostName().equals(toMessage)) {
                            if (distanceVector.containsDestiny(toMessage)) {
                                String exitInterfaceDNS = distanceVector.getHostWithLessCost(toMessage);
                                if (!exitInterfaceDNS.isEmpty()) {
                                    String ipExitInterface = getKeyByValue(dnsTable, exitInterfaceDNS);
                                    if (ipExitInterface != null) {
                                        InetAddress address = InetAddress.getByName(ipExitInterface);
                                        Socket socket = new Socket(address, FORWARDING_SERVER_PORT);
                                        OUT = new PrintWriter(socket.getOutputStream());
                                        OUT.write(FROM_CONSTANT + ":" + fromMessage + "\n");
                                        OUT.flush();
                                        OUT.write(TO_CONSTANT + ":" + toMessage + "\n");
                                        OUT.flush();
                                        OUT.write(MSG_CONSTANT+":"+message+"\n");
                                        OUT.flush();
                                        OUT.write(EOF_CONSTANT+"\n");
                                        OUT.flush();
                                        System.out.println("Msg sent successful");
                                    }
                                }
                            }
                        }else{
                            //entregar a la aplicacion
                        }
                    }else{
                        System.out.println("Error, message 'Msg' bad formed");
                    }
                }else{
                    System.out.println("Error, message 'To' bad formed");
                }
            }else{
                System.out.println("Error, message 'From' bad formed");
            }
        }catch (Exception e){
            System.out.println("<ERROR> Caused By: " + e);
        }
    }
}
