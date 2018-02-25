package controller;

import model.ClientBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientConsole;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private static final Logger LOGGER = LogManager.getLogger();

    private Socket socket = null;
    private ClientBase base;
    private Scanner reader;
    private PrintWriter writer;

    public ClientController(ClientBase base){
        this.base = base;
    }

    public void connect() throws IOException {
       socket = new Socket(base.getHost(),base.getPort());
       reader = new Scanner(socket.getInputStream(), "UTF-8");
       writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
    }

    public Scanner getScanner() {
        return reader;
    }

    public void sendUserData(String request){
        writer.println(request);
    }

    public void sendMessage(String message){
        writer.println(message);
    }

    public void receive(){
        String str1 = reader.nextLine();
        String str2 = reader.nextLine();
        String str3 = reader.nextLine();
        LOGGER.log(Level.INFO, " - ["+str1+" "+str2+"] : "+str3);
    }

    public void close(){
        if(writer != null){
            writer.close();
        }
        if(reader != null){
            reader.close();
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
