package view;

import controller.ClientController;
import controller.ReadFromServer;
import controller.WriteToServer;
import java.util.Scanner;

public class ClientConsole {

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
                    writeToServer.sendPacket(line, 1);
                    continue;
                }

                writeToServer.sendPacket(line, 2);

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
}
