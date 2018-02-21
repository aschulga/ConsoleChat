package controller;

import model.Parameters;
import model.ServerBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;

public class ServerController {

    private static final int REQUEST = 1;
    private ServerSocket serverSocket;
    private ServerBase base;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ServerController(ServerBase base){
        this.base = base;
    }

    public LinkedList<Socket> getListAgent(){
        return base.getListAgent();
    }

    public LinkedList<Socket> getListClient(){
        return base.getListClient();
    }

    public Map<Socket, Parameters<Socket, Boolean>> getMapPair() {
        return base.getMapPair();
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return base.getMapParameters();
    }

    public void connect(){
        try {
            serverSocket = new ServerSocket(base.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitClients() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket, this);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }




}
