package controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class WriteToServer {

    private static final Logger LOGGER = LogManager.getLogger();

    private ClientController controller;
    private boolean isRegistration = true;

    public WriteToServer(ClientController controller){
        this.controller = controller;
    }

    public void send(){

        Scanner scanner = new Scanner(System.in);
        String line;

        while (true) {
            if (scanner.hasNextLine()) {
                line = scanner.nextLine();

                if (isRegistration) {
                    if (!Validator.isValidateRequest(line)) {
                        LOGGER.log(Level.INFO, " - Invalid command");
                        continue;
                    } else {
                        controller.sendUserData(line);
                        isRegistration = false;
                    }
                }
                else {
                    controller.sendMessage(line);
                }
            }
            else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    LOGGER.catching(e);
                }
            }
        }
    }
}
