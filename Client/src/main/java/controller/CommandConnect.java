package controller;

import java.io.IOException;

public class CommandConnect implements Command {

    private ClientController controller;

    public CommandConnect(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.connect();
        } catch (IOException e) {
            System.out.println("Connect Client: "+e);
        }
    }
}
