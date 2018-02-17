package controller;


import java.io.DataOutputStream;
import java.io.IOException;

public class WriteToServer {

    private ClientController controller;

    public WriteToServer(ClientController controller){
        this.controller = controller;
    }

    public void sendPacket(String line, int number) {//передаю сообщения серверу

        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(controller.getSocket().getOutputStream());

            dos.writeUTF(line);
            dos.writeInt(number);
            dos.flush();//очистка выходного буфера


        } catch (IOException e) {
            e.printStackTrace();
        }
        /*finally {
            if(dos != null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
