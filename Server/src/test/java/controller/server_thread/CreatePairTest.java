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

public class CreatePairTest {

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
    public void createPairListsAreEmpty() {
        int expectedSize = 0;
        new ServerThread(socket, controller).createPair();
        int size = controller.getMapParameters().size();
        Assert.assertEquals(size, expectedSize);
    }
}
