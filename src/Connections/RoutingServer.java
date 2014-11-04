package Connections;

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
    private static final String  FROM_CONSTANT = "From:";
    private static final String HELLO_CONSTANT = "Type:HELLO";
    private static final String WELCOME_CONSTANT = "Type:WELCOME";
    private static final String DV_CONSTANT = "Type:DV";
    private static ServerSocket serverSocket;
    private static BufferedReader IN;
    private static PrintWriter OUT_WRITER;
    private static String ADYACENT_IP;

    public RoutingServer(String ip) throws Exception{
        serverSocket = new ServerSocket(ROUTING_SERVER_PORT);
        ADYACENT_IP = ip;
    }

    public void compareMsg(String msg, BufferedReader br) throws  Exception{
        String adyacentIp;
        if (msg.contains(":")){
            String[] firstPick = msg.split(":");
            adyacentIp = firstPick[1];
            if ((!adyacentIp.isEmpty()) && (adyacentIp.equals(ADYACENT_IP))){
                String helloMsg = FROM_CONSTANT + adyacentIp;
                if (msg.equals(helloMsg)){
                    String secondPart = br.readLine();
                    if (secondPart.contains(":")){
                        if (secondPart.equals(HELLO_CONSTANT)){
                            //No se si al instanciar el br aqui de clavos esto
                            //por que no se si el br puede hacer eso
                            OUT_WRITER.write(WELCOME_CONSTANT);
                            System.out.println("MESSAGE SENT: " + WELCOME_CONSTANT);
                            OUT_WRITER.flush();
                        }
                        if (secondPart.equals(DV_CONSTANT)){
                            System.out.print("MEANWHILE, PRINTING SOMETHING");
                        }
                    }
                }
            }
        }
    }

    public void run(){
        try {
            Socket socket = serverSocket.accept();
            IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OUT_WRITER = new PrintWriter(socket.getOutputStream());
            String firstPart = IN.readLine();
            compareMsg(firstPart, IN);
        }catch (IOException ioe){
            System.out.println("<ERROR> Cuased by:" + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
