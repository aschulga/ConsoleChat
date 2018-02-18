package controller;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromServer extends Thread {

    private static final int AUTHORIZATION = 1;
    private static final int MESSAGE = 2;
    private static final int EXIT = 3;

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
                        System.out.println(e);
                    }
                    continue;
                } else {

                    int number = dis.readInt();

                    switch (number) {
                        case AUTHORIZATION:
                            System.out.println(dis.readUTF());
                            break;
                        case MESSAGE:
                            String str1 = dis.readUTF();
                            String str2 = dis.readUTF();
                            String str3 = dis.readUTF();
                            System.out.println("[" + str1 + " " + str2 + "] : " + str3);
                            break;
                        case EXIT:
                            controller.end();
                            break;
                    }
                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        }
        finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}