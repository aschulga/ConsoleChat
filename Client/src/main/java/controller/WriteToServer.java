package controller;

import java.io.DataOutputStream;
import java.io.IOException;

public class WriteToServer {

    private ClientController controller;

    public WriteToServer(ClientController controller){
        this.controller = controller;
    }

    public void sendPacket(String line, int number) {

        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(controller.getSocket().getOutputStream());
            dos.writeUTF(line);
            dos.writeInt(number);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
