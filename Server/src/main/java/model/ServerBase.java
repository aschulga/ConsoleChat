package model;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

public class ServerBase {

    private List<Socket> listAgent = Collections.synchronizedList(new LinkedList<Socket>());
    private List<Socket> listClient = Collections.synchronizedList(new LinkedList<Socket>());
    private Map<Socket, Parameters<Socket, Boolean>> mapPair = Collections.synchronizedMap(new HashMap<>());
    private Map<Socket, Parameters<String, String>> mapParameters = Collections.synchronizedMap(new HashMap<>());
    private int port;

    public ServerBase(int port){
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public List<Socket> getListAgent() {
        return listAgent;
    }

    public List<Socket> getListClient() {
        return listClient;
    }

    public Map<Socket, Parameters<Socket, Boolean>> getMapPair() {
        return mapPair;
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }
}
