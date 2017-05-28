package com.segg3r.spring;

import com.segg3r.spring.entity.StringMessage;
import com.segg3r.spring.tcp.TcpServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TcpControllerTestConfiguration.class)
public class TcpControllerTest {

    @Autowired
    private TcpServer tcpServer;
    @Autowired
    private TestTcpController controller;

    private Socket socket;
    private ObjectOutputStream out;

    @Before
    public void setup() throws Exception {
        int port = tcpServer.getPort();
        socket = new Socket("127.0.0.1", port);
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @After
    public void teardown() throws IOException {
        controller.clear();
        socket.close();
    }

    @Test
    public void testControllerObjectMethod() throws Exception {
        out.writeObject(new StringMessage("Hello, world!"));

        Object message = controller.waitForObject();
        assertTrue(message instanceof StringMessage);
        assertEquals("Hello, world!", ((StringMessage) message).getMessage());
    }

}
