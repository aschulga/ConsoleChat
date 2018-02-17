package controller;

import model.Agent;
import model.Client;
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

        if (controller.getListClient().size() != 0 && controller.getListAgent().size() != 0) {

            Socket s1 = controller.getListClient().removeLast().getSocket();
            Socket s2 = controller.getListAgent().removeLast().getSocket();

            controller.getMapPair().put(s1, s2);
            controller.getMapPair().put(s2, s1);

            LOGGER.log(Level.INFO, " - The beginning of a chat between "+controller.getMapParameters().get(s1).getStatus() + " "+
                    controller.getMapParameters().get(s1).getName()+" and "+controller.getMapParameters().get(s2).getStatus() + " "+
                    controller.getMapParameters().get(s2).getName());
        }
    }

    public void authorize(DataOutputStream dos, String[]strings, int number){

        try {
            dos.writeInt(number);
            dos.writeUTF("We registered you " + strings[1]+" "+strings[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ("agent".equals(strings[1])) {
            controller.getListAgent().push(new Agent(socket));
        } else if ("client".equals(strings[1])) {
            controller.getListClient().push(new Client(socket));
        }

        LOGGER.log(Level.INFO, " - The appearance of "+strings[1]+" "+strings[2]+" in the system");

        controller.getMapParameters().put(socket, new Parameters<>(strings[1],strings[2]));
        createPair();
    }

    public void leave(){

        Socket s = findValue(socket);

        controller.getListAgent().push(new Agent(s));
        controller.getListClient().push(new Client(socket));

        LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getStatus() + " "+
                controller.getMapParameters().get(socket).getName()+" and "+controller.getMapParameters().get(s).getStatus() + " "+
                controller.getMapParameters().get(s).getName());

        controller.getMapPair().remove(s);//delete agent->client, know client
        controller.getMapPair().remove(socket);//delete client->agent, know client
        createPair();

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(3);
            dos.writeUTF(controller.getMapParameters().get(socket).getStatus());
            dos.writeUTF(controller.getMapParameters().get(socket).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endChat(){

        Socket s = findValue(socket);

        LOGGER.log(Level.INFO, " - The end of the chat between " + controller.getMapParameters().get(socket).getStatus() + " " +
                controller.getMapParameters().get(socket).getName() + " and " + controller.getMapParameters().get(s).getStatus() + " " +
                controller.getMapParameters().get(s).getName());
        //////
        controller.getMapPair().remove(s);//delete agent->client, know client
        controller.getMapPair().remove(socket);//delete client->agent, know client
        createPair();

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(4);
            dos.writeUTF(controller.getMapParameters().get(socket).getStatus());
            dos.writeUTF(controller.getMapParameters().get(socket).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.getMapParameters().remove(socket);
    }

    public void exit() {

        if (controller.getMapParameters().get(socket).getStatus().equals("client")) {
            controller.getListAgent().push(new Agent(findValue(socket)));
        } else {
            controller.getListClient().push(new Client(findValue(socket)));
        }

        LOGGER.log(Level.INFO, " - The " + controller.getMapParameters().get(socket).getStatus() + " " +
                controller.getMapParameters().get(socket).getName() + " left the system");
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        String str;
        int number;

        try {

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            while ((str = dis.readUTF()) != null) {

                number = dis.readInt();
                String[]strings = str.split(" ");

                if("/register".equals(strings[0]) && number == 1) {
                    authorize(dos, strings, number);
                }
                else if ("/leave".equals(strings[0]) && controller.getMapParameters().get(socket).getStatus().equals("client")) {
                    if(controller.getMapPair().containsKey(socket) == true) {
                        leave();
                    }
                }
                else if ("/exit".equals(strings[0])){
                    exit();
                    if (controller.getMapPair().containsKey(socket) == true) {
                        endChat();
                    }

                    //socket.close();
                    //System.exit(0);
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
            // если клиент не отвечает, соединение с ним разрывается
            LOGGER.catching(e);
        } finally {
            disconnect(dos, dis);
        }
    }

    public void disconnect(DataOutputStream dos, DataInputStream dis) {
        try {
            if (dos != null) {
                dos.close();
            }
            if (dis != null) {
                dis.close();
            }
        } catch (IOException e) {
            LOGGER.catching(e);
        } finally {
            this.interrupt();
        }
    }
}