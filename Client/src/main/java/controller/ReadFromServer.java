package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientConsole;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int REGISTRATION = 1;
    private static final int LEAVE = 2;
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
                if (controller.getDataInputStream().available() <= 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        LOGGER.catching(e);
                    }
                    continue;
                } else {
                    int number = controller.getDataInputStream().readInt();

                    switch (number) {
                        case REGISTRATION: {
                            break;
                        }
                        case LEAVE: {
                            break;
                        }
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
        } catch (IOException e) {
            LOGGER.catching(e);
        } finally {
            controller.close();
        }
    }
}








