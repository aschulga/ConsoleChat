package controller;

import model.ClientBase;

import java.io.IOException;
import java.net.Socket;

public class ClientController {

    private Socket socket = null;
    private ClientBase base;

    public ClientController(ClientBase base){
        this.base = base;
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect(){
        try {
            socket = new Socket(base.getHost(),base.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void end(){
        try {
            if(socket != null) {
                socket.close();
            }
        }catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
