package controller.server_thread;

import controller.ServerController;
import controller.ServerThread;
import model.ServerBase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.Socket;

public class AuthorizeTest {

    ServerController controller;
    Socket socket;

    @BeforeClass
    public void createSocket() {

        ServerBase base = new ServerBase(8071);
        controller = new ServerController(base);
        socket = new Socket();
    }

    @AfterClass
    public void closeSocket() throws IOException {
        socket.close();
    }

    @Test
    public void authorizeAgent() {

        int expectedSize = 1;

        new ServerThread(socket, controller).authorize("agent", "Alex");
        int size = controller.getListAgent().size();

        Assert.assertEquals(size, expectedSize);
    }

    @Test
    public void authorizeClient() {

        int expectedSize = 1;

        new ServerThread(socket, controller).authorize("client", "Alex");
        int size = controller.getListClient().size();

        Assert.assertEquals(size, expectedSize);
    }

    @Test
    public void authorizePutInMapParameters() {

        int expectedSize = 1;

        new ServerThread(socket, controller).authorize("client", "Alex");
        int size = controller.getMapParameters().size();

        Assert.assertEquals(size, expectedSize);
    }

}
