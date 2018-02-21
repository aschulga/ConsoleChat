package controller;

import java.io.IOException;

public class CommandRegistration implements Command{
    private ClientController controller;
    private String request;
    private int number;

    public CommandRegistration(String request, ClientController controller, int number){
        this.request = request;
        this.controller = controller;
        this.number = number;
    }

    @Override
    public void execute() {
        try {
            controller.sendUserData(request, number);
        } catch (IOException e) {
            System.out.println("DataOutput Client: "+e);
        }
    }
}
