package connections;

import java.io.BufferedReader;
import java.io.IOException;
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
    private static final String LEN_CONSTANT = "Len:";

    private BufferedReader IN;
    private PrintWriter OUT;
    private static String MY_IP;
    private String adjacentHostIP;
    private boolean newMessage;


    public ReadingMessages(Socket socket, String localIp, String adjacentIP) throws Exception{
        IN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OUT = new PrintWriter(socket.getOutputStream());
        MY_IP =localIp;
        newMessage = false;
        this.adjacentHostIP = adjacentIP;
    }

    public void getDV(BufferedReader dvBr, int sizeOfList, String source) throws IOException {
        for (int i=0; i<= sizeOfList; i++){
            String dv;
            if ((dv = dvBr.readLine()) != null){
                if (dv.contains(":")){
                    System.out.println("<SERVER> DV RECIEVED: " + dv);
                    //Agregar el metodo que agrega los dv a la tabla, source
                    //es de donde viene el mensaje.
                }
            }
        }
    }

    public void compareMsg(String msg, BufferedReader br) throws  Exception{
        String adyacentIp;
        if (msg.contains(":")){
            System.out.println("<SERVER>RECIEVING FROM CLIENT:" + msg);
            String[] firstPick = msg.split(":");
            adyacentIp = firstPick[1];
            if ((!adyacentIp.isEmpty())){
                String helloMsg = FROM_CONSTANT + adyacentIp;
                if (msg.equals(helloMsg)){
                    String secondPart = br.readLine();
                    if (secondPart.contains(":")){
                        //Comparo si es Tipo HELLO
                        if (secondPart.equals(HELLO_CONSTANT)){
                            System.out.println("<SERVER> MESSAGE RECEIVED: " + helloMsg + HELLO_CONSTANT);
                            String from = FROM_CONSTANT + MY_IP + "\n";
                            String weclomeMsg = WELCOME_CONSTANT + "\n";
                            OUT.write(from + weclomeMsg);
                            System.out.println("<SERVER>MESSAGE SENT: " + from + weclomeMsg);
                            OUT.flush();
                        }
                        //Comparo si es Tipo DV
                        if (secondPart.equals(DV_CONSTANT)){
                            System.out.print("<SERVER> MESSAGE RECIEVED: " + DV_CONSTANT);
                            String msgLength = br.readLine();
                            if (msg.equals(LEN_CONSTANT)) {
                                String[] splitLenght = msgLength.split(":");
                                String listSize = splitLenght[1];
                                int size = Integer.parseInt(listSize);
                                getDV(br, size, adyacentIp);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean existsNewMessage(){
        return this.newMessage;
    }

    public void resetNewMessageFlag(){
        this.newMessage = false;
    }

    public String getAdjacentIP(){ return this.adjacentHostIP;}

    public void run(){
        try {
            while(true){
                String clientMsg = IN.readLine();
                this.newMessage = true;
                compareMsg(clientMsg, IN);
            }
        }catch (Exception e){
            System.out.println("<ERROR> Caused By: " + e);
        }
    }

}
