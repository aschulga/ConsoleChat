package model;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ServerBase {

    private LinkedList<Socket> listAgent = new LinkedList<Socket>();
    private LinkedList<Socket> listClient = new LinkedList<Socket>();
    private Map<Socket, Socket> mapPair = new HashMap<>();
    private Map<Socket, Parameters<String, String>> mapParameters = new HashMap<>();
    private int port;

    public ServerBase(int port){
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public LinkedList<Socket> getListAgent() {
        return listAgent;
    }

    public LinkedList<Socket> getListClient() {
        return listClient;
    }

    public Map<Socket, Socket> getMapPair() {
        return mapPair;
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }
}
