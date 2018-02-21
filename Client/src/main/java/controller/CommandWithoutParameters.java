package controller;

import java.io.IOException;

public class CommandWithoutParameters implements Command {

    private static final int LEAVE = 2;
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private ClientController controller;
    private String request;


    public CommandWithoutParameters(String request, ClientController controller) {
        this.request = request;
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            if (Validator.isValidateRequest(request, LEAVE)) {
                controller.sendCommandWithoutParameters(LEAVE);
            }
            else if (Validator.isValidateRequest(request, EXIT)) {
                controller.sendCommandWithoutParameters(EXIT);
                //controller.end();
                //ClientConsole.exit();
            }
            else {
                controller.sendMessage(request, MESSAGE);
            }
        } catch (IOException e) {
            System.out.println("DataOutput Client: "+e);
        }
    }
}
