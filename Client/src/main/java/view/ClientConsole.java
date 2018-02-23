package view;

import controller.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ClientConsole {

    private static final Logger LOGGER = LogManager.getLogger();
    private ClientController controller;

    public ClientConsole(ClientController controller)
    {
        this.controller = controller;
    }

    public void init(){

        try {
            controller.connect();
        } catch (IOException e) {
            LOGGER.catching(e);
        }

        ReadFromServer readFromServer = new ReadFromServer(controller);
        readFromServer.start();

        WriteToServer writeToServer = new WriteToServer(controller);
        writeToServer.send();
    }

    public static void exit(){
        System.exit(0);
    }
}
