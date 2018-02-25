package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadFromServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private ClientController controller;

    public ReadFromServer(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!controller.getScanner().hasNextLine()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        LOGGER.catching(e);
                    }
                    continue;
                }
                else {
                    int number = Integer.parseInt(controller.getScanner().nextLine());

                    switch (number) {
                        case EXIT: {
                            controller.close();
                            break;
                        }
                        case MESSAGE: {
                            controller.receive();
                            break;
                        }
                    }
                }
            }
        } finally {
            controller.close();
        }
    }
}








