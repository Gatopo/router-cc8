package Routing;

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
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static BufferedReader IN;
    private static PrintWriter OUT;

    public RoutingServer() throws Exception{
        serverSocket = new ServerSocket(ROUTING_SERVER_PORT);
    }

    public boolean waitForConection() throws Exception{
        socket = serverSocket.accept();
        IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return true;
    }

    public void run(){
        try {
            String helloMsg = IN.readLine();
            if (helloMsg.contains(FROM_CONSTANT)) {
                String[] fromFragment = helloMsg.split(":");
                String adyacentNode = fromFragment[1];
                if (!adyacentNode.isEmpty()) {
                    helloMsg = IN.readLine();
                    if (helloMsg.equals(HELLO_CONSTANT)){
                        OUT.println(WELCOME_CONSTANT);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
