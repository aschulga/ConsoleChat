package controller;

import model.ClientBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientConsole;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    private static final Logger LOGGER = LogManager.getLogger();

    private Socket socket = null;
    private ClientBase base;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientController(ClientBase base){
        this.base = base;
    }

    public void connect() throws IOException {
       socket = new Socket(base.getHost(),base.getPort());
       dis = new DataInputStream(socket.getInputStream());
       dos = new DataOutputStream(socket.getOutputStream());
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public void sendUserData(String request, int number) throws IOException {
        String[]string = request.split(" ");
        dos.writeUTF(String.valueOf(number));
        dos.writeUTF(string[1]);
        dos.writeUTF(string[2]);
        dos.flush();
    }

    public void receiveAnswerServerForRegistration() throws IOException {
        LOGGER.log(Level.INFO, " - "+dis.readUTF());
    }

    public void sendCommandWithoutParameters(int number) throws IOException {
        dos.writeUTF(String.valueOf(number));
    }

    public void receiveAnswerForCommandLeave() throws IOException {
        String str1 = dis.readUTF();
        String str2 = dis.readUTF();
        LOGGER.log(Level.INFO, " - "+str1+" "+str2+" leave");
    }

    public void receiveAnswerForCommandExit() throws IOException {
        String str1 = dis.readUTF();
        String str2 = dis.readUTF();
        LOGGER.log(Level.INFO, " - "+str1+" "+str2+" exit");
    }

    public void sendMessage(String message, int number) throws IOException {
        dos.writeUTF(String.valueOf(number));
        dos.writeUTF(message);
    }

    public void receive() throws IOException {
        String str1 = dis.readUTF();
        String str2 = dis.readUTF();
        String str3 = dis.readUTF();
        LOGGER.log(Level.INFO, " - ["+str1+" "+str2+"] : "+str3);
    }

    public void close(){
        try{
            if(dos != null){
                dos.close();
            }
        }catch(IOException e){
            LOGGER.catching(e);
        }

        try{
            if(dis != null){
                dis.close();
            }
        }catch(IOException e){
            LOGGER.catching(e);
        }

        try{
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            LOGGER.catching(e);
        }

        ClientConsole.exit();
    }
}
