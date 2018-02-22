package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CommandRegistration implements Command{

    private static final Logger LOGGER = LogManager.getLogger();
    private ClientController controller;
    private String request;
    private int number;

    public CommandRegistration(String request, ClientController controller, int number){
        this.request = request;
        this.controller = controller;
        this.number = number;
    }

    @Override
    public void execute() {
        try {
            controller.sendUserData(request, number);
        } catch (IOException e) {
            LOGGER.catching(e);
            controller.closeDos();
        }
    }
}
