package controller;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromServer extends Thread {

    private ClientController controller;

    public ReadFromServer(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void run() {

        DataInputStream dis = null;

        while (true){
            try{
                dis = new DataInputStream(controller.getSocket().getInputStream());
                if(dis.available() <= 0){
                    try{
                        Thread.sleep(10);
                    }catch(InterruptedException e){
                        System.out.println(e);
                    }
                    continue;
                }
                else{

                    int number = dis.readInt();

                    switch(number){
                        case 1:
                            System.out.println(dis.readUTF());
                            break;
                        case 2:{
                            String str1 = dis.readUTF();
                            String str2 = dis.readUTF();
                            String str3 = dis.readUTF();
                            System.out.println("["+str1+" "+str2+"] : "+str3);
                            break;
                        }
                        case 3:{
                            String str1 = dis.readUTF();
                            String str2 = dis.readUTF();
                            System.out.println("[" + str1 + " " + str2 +" leave]");
                            break;
                        }
                        case 4:{
                            String str1 = dis.readUTF();
                            String str2 = dis.readUTF();
                            System.out.println("[" + str1 + " " + str2 +" exit]");
                            break;
                        }
                    }
                }
            }catch(IOException e){
                System.out.println(e);
                break;
            }
            /*finally {
                if(dis != null){
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }
    }
}