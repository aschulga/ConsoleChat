package model;

import java.net.Socket;

public class Agent {

    private Socket socket;

    public Agent(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
