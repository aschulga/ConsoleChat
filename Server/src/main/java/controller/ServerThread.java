package controller;

import model.Parameters;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int COMMAND = 0;
    private static final int AUTHORIZATION = 1;
    private static final int STATUS = 1;
    private static final int NAME = 2;
    private static final int EXIT = 3;
    private static final String REGISTRATIONCOMMAND = "/register";
    private static final String LEAVECHATCOMMAND = "/leave";
    private static final String EXITCHATCOMMAND = "/exit";

    private Socket socket;
    private ServerController controller;

    public ServerThread(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    public Socket findValue(Socket socket) {
        return controller.getMapPair().get(socket);
    }

    public void createPair(){
        if (!controller.getListClient().isEmpty() && !controller.getListAgent().isEmpty()) {

            Socket s1 = controller.getListClient().removeLast();
            Socket s2 = controller.getListAgent().removeLast();

            controller.getMapPair().put(s1, s2);
            controller.getMapPair().put(s2, s1);

            LOGGER.log(Level.INFO, " - The beginning of a chat between "+controller.getMapParameters().get(s1).getStatus()+" "+
                    controller.getMapParameters().get(s1).getName()+" and "+controller.getMapParameters().get(s2).getStatus()+" "+
                    controller.getMapParameters().get(s2).getName());
        }
    }

    public void authorize(String[]strings){
        if ("agent".equals(strings[STATUS])) {
            controller.getListAgent().push(socket);
        } else if ("client".equals(strings[STATUS])) {
            controller.getListClient().push(socket);
        }

        LOGGER.log(Level.INFO, " - The appearance of "+strings[STATUS]+" "+strings[NAME]+" in the system");

        controller.getMapParameters().put(socket, new Parameters<>(strings[STATUS],strings[NAME]));
        createPair();
    }

    public void leave(){
        Socket s = findValue(socket);

        controller.getListAgent().push(s);
        controller.getListClient().push(socket);

        LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getStatus()+" "+
                controller.getMapParameters().get(socket).getName()+" and "+controller.getMapParameters().get(s).getStatus()+" "+
                controller.getMapParameters().get(s).getName());

        controller.getMapPair().remove(s);
        controller.getMapPair().remove(socket);
        createPair();
    }

    public void exit() {
        if (controller.getMapPair().containsKey(socket) == true) {

            if (controller.getMapParameters().get(socket).getStatus().equals("client"))
                controller.getListAgent().push(findValue(socket));
            else
                controller.getListClient().push(findValue(socket));

            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getStatus()+" "+
                    controller.getMapParameters().get(socket).getName()+" left the system");

            Socket s = findValue(socket);

            LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getStatus()+" "+
                    controller.getMapParameters().get(socket).getName()+" and "+controller.getMapParameters().get(s).getStatus()+" "+
                    controller.getMapParameters().get(s).getName());

            controller.getMapPair().remove(s);
            controller.getMapPair().remove(socket);
            controller.getMapParameters().remove(socket);
            createPair();
        }
        else {
            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getStatus()+" "+
                    controller.getMapParameters().get(socket).getName()+" left the system");

            if(controller.getMapParameters().get(socket).getStatus().equals("client"))
                controller.getListClient().remove(socket);
            else
                controller.getListAgent().remove(socket);

            controller.getMapParameters().remove(socket);
        }
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        String str;
        int number;

        try {

            dis = new DataInputStream(socket.getInputStream());

            while ((str = dis.readUTF()) != null) {

                dos = new DataOutputStream(socket.getOutputStream());
                number = dis.readInt();
                String[]strings = str.split(" ");

                if(REGISTRATIONCOMMAND.equals(strings[COMMAND]) && number == AUTHORIZATION) {
                    authorize(strings);
                }
                else if (LEAVECHATCOMMAND.equals(strings[COMMAND]) && controller.getMapParameters().get(socket).getStatus().equals("client")) {
                    if(controller.getMapPair().containsKey(socket) == true) {
                        leave();
                    }
                }
                else if (EXITCHATCOMMAND.equals(strings[COMMAND])){
                    exit();
                    dos.writeInt(EXIT);
                }
                else {
                    while (true) {
                        if (controller.getMapPair().containsKey(socket) == true) {
                            dos = new DataOutputStream(findValue(socket).getOutputStream());
                            dos.writeInt(number);
                            dos.writeUTF(controller.getMapParameters().get(socket).getStatus());
                            dos.writeUTF(controller.getMapParameters().get(socket).getName());
                            dos.writeUTF(str);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            disconnect(dos, dis);
        }
    }

    public void disconnect(DataOutputStream dos, DataInputStream dis) {

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        if (dis != null) {
            try {
                dis.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}