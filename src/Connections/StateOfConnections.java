package connections;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 15/11/14.
 */
public class StateOfConnections {

    private static Map<String, ConnectionState> connections;

    public StateOfConnections(){
        connections = new HashMap<String, ConnectionState>();
    }

    public void addNewConnection(String ip, boolean stateOfConnection, RoutingClient routingClient){
        ConnectionState connectionState = new ConnectionState(routingClient, stateOfConnection);
        connections.put(ip, connectionState);
    }

    public boolean hasConnection(String ip){
        return connections.containsKey(ip);
    }

    public boolean getStateOfConnection(String ip){
        return connections.get(ip).getStateOfConnection();
    }

    public void changeStateOfConnection(String ip, boolean state){
        connections.get(ip).setStateOfConnection(state);
    }

    public RoutingClient getClient(String ip){
        return connections.get(ip).getClient();
    }
}
