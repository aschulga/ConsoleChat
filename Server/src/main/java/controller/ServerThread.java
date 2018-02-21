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
    private static final int REGISTRATION = 1;
    private static final int LEAVE = 2;
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private Socket socket;
    private ServerController controller;

    public ServerThread(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    public Socket findValue(Socket socket) {
        return controller.getMapPair().get(socket).getParameter1();
    }

    public void createPair(){
        if (!controller.getListClient().isEmpty() && !controller.getListAgent().isEmpty()) {

            Socket s1 = controller.getListClient().removeLast();
            Socket s2 = controller.getListAgent().removeLast();

            controller.getMapPair().put(s1, new Parameters<>(s2, true));//true
            controller.getMapPair().put(s2, new Parameters<>(s1, false));//false

            LOGGER.log(Level.INFO, " - The beginning of a chat between "+controller.getMapParameters().get(s1).getParameter1()+" "+
                    controller.getMapParameters().get(s1).getParameter2()+" and "+controller.getMapParameters().get(s2).getParameter1()+" "+
                    controller.getMapParameters().get(s2).getParameter2());
        }
    }

    public void authorize(String status, String name){
        if ("agent".equals(status)) {
            controller.getListAgent().push(socket);
        } else if ("client".equals(status)) {
            controller.getListClient().push(socket);
        }

        LOGGER.log(Level.INFO, " - The appearance of "+status+" "+name+" in the system");

        controller.getMapParameters().put(socket, new Parameters<>(status, name));
        createPair();
    }

    public void leave(){
        Socket s = findValue(socket);

        controller.getListAgent().push(s);
        controller.getListClient().push(socket);

        LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getParameter1()+" "+
                controller.getMapParameters().get(socket).getParameter2()+" and "+controller.getMapParameters().get(s).getParameter1()+" "+
                controller.getMapParameters().get(s).getParameter2());

        controller.getMapPair().remove(s);
        controller.getMapPair().remove(socket);
        createPair();
    }

    public void exit() {
        if (controller.getMapPair().containsKey(socket) == true) {

            if (controller.getMapParameters().get(socket).getParameter1().equals("client"))
                controller.getListAgent().push(findValue(socket));
            else
                controller.getListClient().push(findValue(socket));

            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" left the system");

            Socket s = findValue(socket);

            LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" and "+controller.getMapParameters().get(s).getParameter1()+" "+
                    controller.getMapParameters().get(s).getParameter2());

            controller.getMapPair().remove(s);
            controller.getMapPair().remove(socket);
            controller.getMapParameters().remove(socket);
            createPair();
        }
        else {
            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" left the system");

            if(controller.getMapParameters().get(socket).getParameter1().equals("client"))
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
        String codeCommand;
        String str;
        try {

            dis = new DataInputStream(socket.getInputStream());

            while ((codeCommand = dis.readUTF()) != null) {

                dos = new DataOutputStream(socket.getOutputStream());

                if (Integer.parseInt(codeCommand) == REGISTRATION) {
                    authorize(dis.readUTF(), dis.readUTF());
                }
                else if (Integer.parseInt(codeCommand) == LEAVE && controller.getMapParameters().get(socket).getParameter1().equals("client")) {
                    if (controller.getMapPair().containsKey(socket) == true) {
                        leave();
                    }
                }
                else if (Integer.parseInt(codeCommand) == EXIT) {
                    exit();
                    dos.writeInt(EXIT);
                }
                else if (Integer.parseInt(codeCommand) == MESSAGE){
                    str = dis.readUTF();

                    while (true) {
                        if (controller.getMapPair().containsKey(socket)) {
                            if(controller.getMapPair().get(socket).getParameter2()) {
                                dos = new DataOutputStream(findValue(socket).getOutputStream());
                                dos.writeInt(MESSAGE);
                                dos.writeUTF(controller.getMapParameters().get(socket).getParameter1());
                                dos.writeUTF(controller.getMapParameters().get(socket).getParameter2());
                                dos.writeUTF(str);

                                controller.getMapPair().get(findValue(socket)).setParameter2(true);
                            }
                            break;
                        }
                    }
                }
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        finally {
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