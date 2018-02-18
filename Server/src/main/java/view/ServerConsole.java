package view;

import controller.ServerController;

public class ServerConsole {

    private ServerController controller;

    public ServerConsole(ServerController controller) {
        this.controller = controller;
    }

    public void init(){
        controller.connect();
        System.out.println("inizialized");
        controller.waitClients();
    }
}
