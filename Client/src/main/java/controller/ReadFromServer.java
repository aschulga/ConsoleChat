package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int REGISTRATION = 1;
    private static final int LEAVE = 2;
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private ClientController controller;

    public ReadFromServer(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            while (true) {
                dis = new DataInputStream(controller.getSocket().getInputStream());
                if (dis.available() <= 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        LOGGER.catching(e);
                    }
                    continue;
                } else {

                    int number = dis.readInt();

                    switch (number) {
                        case REGISTRATION:{
                            controller.receiveAnswerServerForRegistration();
                            break;
                        }
                        case LEAVE: {
                            controller.receiveAnswerForCommandLeave();
                            break;
                        }
                        case EXIT: {
                            //controller.receiveAnswerForCommandExit();
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
        }
        catch(IOException e) {
            LOGGER.catching(e);
        }
        finally {
            controller.closeDis();
        }
    }
}








