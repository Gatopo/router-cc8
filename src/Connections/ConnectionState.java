package connections;

import java.net.Socket;

/**
 * Created by mario on 15/11/14.
 */
public class ConnectionState {

    private RoutingClient routingClient;
    private boolean state;

    public ConnectionState(RoutingClient routingClient, boolean stateOfConnection){
        this.routingClient = routingClient;
        this.state = stateOfConnection;
    }

    public boolean getStateOfConnection(){
        return state;
    }

    public RoutingClient getClient(){
        return this.routingClient;
    }

    public void setStateOfConnection(boolean stateOfConnection){
        state = stateOfConnection;
    }
}
