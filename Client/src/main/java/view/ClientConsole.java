package view;

import controller.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class ClientConsole {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int REGISTRATION = 1;

    private ClientController controller;
    private boolean isRegistration = true;


    public ClientConsole(ClientController controller)
    {
        this.controller = controller;
    }

    public void init(){

        CommandConnect commandConnect = new CommandConnect(controller);
        commandConnect.execute();

        ReadFromServer readFromServer = new ReadFromServer(controller);
        readFromServer.start();

        Scanner scanner = new Scanner(System.in);
        String line;

        while(true){
            if(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if(isRegistration){
                    if (!Validator.isValidateRequest(line, REGISTRATION)) {
                        LOGGER.log(Level.INFO, " - Invalid command");
                        continue;
                    }
                    else{
                        Command commandRegistration = new CommandRegistration(line, controller, REGISTRATION);
                        commandRegistration.execute();
                        isRegistration = false;
                    }
                }
                else{
                    Command commandLeaveOrExit = new CommandWithoutParameters(line, controller);
                    commandLeaveOrExit.execute();
                }
            }else
            {
                try{
                    Thread.sleep(10);
                }catch(InterruptedException e){
                    LOGGER.catching(e);
                }
            }
        }
    }

    public static void exit(){
        System.exit(0);
    }
}
