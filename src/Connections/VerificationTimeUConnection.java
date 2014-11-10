package connections;

import java.util.ArrayList;

/**
 * Created by Steven on 09/11/2014.
 */
public class VerificationTimeUConnection extends Thread {

    private ReadingMessages serverClientConnection;
    private static long timer;

    public VerificationTimeUConnection(ReadingMessages clientConnection, long timer){
        this.serverClientConnection = clientConnection;
        this.timer = timer;
    }

    public void run(){
        while(true){
            try {
                sleep(timer);
                if(serverClientConnection.existsNewMessage()) {
                    serverClientConnection.resetNewMessageFlag();
                    System.out.println("New Message state received, reset flag and sleep again");
                }else{
                    System.out.println("No new Message review for next default time");
                }
            }catch(InterruptedException ie){
                System.err.println("Error while try to wait for a configured T time, give this error" + ie);
            }
        }
    }
}
