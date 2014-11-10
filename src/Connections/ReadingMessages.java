package connections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by mario on 8/11/14.
 */
public class ReadingMessages extends Thread {
    private static final String  FROM_CONSTANT = "From:";
    private static final String HELLO_CONSTANT = "Type:HELLO";
    private static final String WELCOME_CONSTANT = "Type:WELCOME";
    private static final String DV_CONSTANT = "Type:DV";
    private BufferedReader IN;
    private PrintWriter OUT;
    private static String MY_IP;


    public ReadingMessages(Socket socket, String localIp) throws Exception{
        IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OUT = new PrintWriter(socket.getOutputStream());
        MY_IP =localIp;
    }

    public void compareMsg(String msg, BufferedReader br) throws  Exception{
        String adyacentIp;
        if (msg.contains(":")){
            System.out.println("<SERVER>RECIEVING FROM CLIENT:" + msg);
            String[] firstPick = msg.split(":");
            adyacentIp = firstPick[1];
            if ((!adyacentIp.isEmpty())){
                String helloMsg = FROM_CONSTANT + adyacentIp;
                if (msg.equals(helloMsg)){                  //Creo que comparo si es la misma IP adyacente ...
                    String secondPart = br.readLine();
                    if (secondPart.contains(":")){
                        //Comparo si es Tipo HELLO
                        if (secondPart.equals(HELLO_CONSTANT)){
                            //No se si al instanciar el br aqui de clavos esto
                            //por que no se si el br puede hacer eso
                            System.out.println("<SERVER> MESSAGE RECEIVED: " + helloMsg + HELLO_CONSTANT);
                            String from = FROM_CONSTANT + MY_IP + "\n";
                            String weclomeMsg = WELCOME_CONSTANT + "\n";
                            OUT.write(from + weclomeMsg);
                            System.out.println("<SERVER>MESSAGE SENT: " + from + weclomeMsg);
                            OUT.flush();
                        }
                        //Comparo si es Tipo DV
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
            while(true){
                String clientMsg = IN.readLine();
                compareMsg(clientMsg, IN);
            }
        }catch (Exception e){
            System.out.println("<ERROR> Caused By: " + e);
        }
    }

}
