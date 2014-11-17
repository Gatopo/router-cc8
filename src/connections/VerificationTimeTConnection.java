package connections;

import java.util.ArrayList;

/**
 * Created by Steven on 09/11/2014.
 */
public class VerificationTimeTConnection extends Thread {

    private ReadingMessages serverClientConnection;
    private static StateOfConnections stateOfConnections;
    private static long TIMER_T;
    private static long TIMER_U;

    public VerificationTimeTConnection(ReadingMessages clientConnection, long timerT, long timerU, StateOfConnections soc){
        this.serverClientConnection = clientConnection;
        this.TIMER_T = timerT;
        this.TIMER_U = timerU;
        this.stateOfConnections = soc;
    }

    public void run(){
        while(true){
            try {
                sleep(TIMER_T);
                if(serverClientConnection.existsNewMessage()) {
                    serverClientConnection.resetNewMessageFlag();
                    System.out.println("New Message state received, reset flag and sleep again");
                }else{
                    System.out.println("T time expired, checking for next timer");
                    sleep(TIMER_U);
                    if(serverClientConnection.existsNewMessage()){
                        serverClientConnection.resetNewMessageFlag();
                    }
                    System.out.println("U time expired");
                    stateOfConnections.changeStateOfConnection(serverClientConnection.getAdjacentIP(), false);
                    break;
                }
            }catch(InterruptedException ie){
                System.err.println("Error, timer exception occurred: " + ie);
            }
        }
    }
}
