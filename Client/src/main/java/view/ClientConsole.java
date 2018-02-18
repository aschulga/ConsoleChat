package view;

import controller.ClientController;
import controller.ReadFromServer;
import controller.WriteToServer;
import java.util.Scanner;

public class ClientConsole {

    private static final int AUTHORIZATION = 1;
    private static final int MESSAGE = 2;

    private ClientController controller;
    private boolean agentOrClient = true;

    public ClientConsole(ClientController controller)
    {
        this.controller = controller;
    }

    public void init(){

        controller.connect();

        ReadFromServer readFromServer = new ReadFromServer(controller);
        readFromServer.start();

        WriteToServer writeToServer = new WriteToServer(controller);

        Scanner scanner = new Scanner(System.in);
        String line;

        while(true){
            if(scanner.hasNextLine()) {

                line = scanner.nextLine();

                if(agentOrClient){
                    agentOrClient = false;
                    writeToServer.sendPacket(line, AUTHORIZATION);
                    continue;
                }

                writeToServer.sendPacket(line, MESSAGE);
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
