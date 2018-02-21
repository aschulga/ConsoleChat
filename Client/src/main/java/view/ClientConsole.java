package view;

import controller.*;

import java.util.Scanner;

public class ClientConsole {

    private static final int REGISTRATION = 1;

    private ClientController controller;
    private boolean agentOrClient = true;

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
                if(agentOrClient){
                    if (!Validator.isValidateRequest(line, REGISTRATION)) {
                        System.out.println("Not correct");
                        continue;
                    }
                    else{
                        Command commandRegistration = new CommandRegistration(line, controller, REGISTRATION);
                        commandRegistration.execute();
                        agentOrClient = false;
                    }
                }
                else{
                    Command commandLeafOrExit = new CommandWithoutParameters(line, controller);
                    commandLeafOrExit.execute();
                }
            }else
            {
                try{
                    Thread.sleep(10);
                }catch(InterruptedException e){
                    System.out.println(e);
                }
            }
        }
    }

    public static void exit(){
        System.exit(0);
    }
}
