package controller;

import model.Agent;
import model.Client;
import model.Parameters;
import model.ServerBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;

public class ServerController {

    private static final Logger LOGGER = LogManager.getLogger();

    private ServerSocket serverSocket;
    private ServerBase base;

    public ServerController(ServerBase base){
        this.base = base;
    }

    public LinkedList<Agent> getListAgent(){
        return base.getListAgent();
    }

    public LinkedList<Client> getListClient(){
        return base.getListClient();
    }

    public Map<Socket, Socket> getMapPair() {
        return base.getMapPair();
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return base.getMapParameters();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
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
                //ожидание клиента
                Socket socket = serverSocket.accept();
                System.out.println(serverSocket.getInetAddress().getHostName() + " connected ");

                //создание отдельного потока для обмена данными
                //с соединившимся клиентом

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(1);
                dos.writeUTF("Who are you?(client or agent)");

                ServerThread thread = new ServerThread(socket, this);
                //запуск потока
                thread.start();
            }
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }

    public void end()
    {
        try {
            serverSocket.close();
        }catch (IOException e)
        {
            LOGGER.catching(e);
        }
    }

}
