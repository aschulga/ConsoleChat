package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class CommandConnect implements Command {

    private static final Logger LOGGER = LogManager.getLogger();
    private ClientController controller;

    public CommandConnect(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.connect();
        } catch (IOException e) {
            LOGGER.catching(e);
            controller.close();
        }
    }
}
