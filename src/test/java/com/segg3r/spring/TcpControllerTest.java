package com.segg3r.spring;

import com.segg3r.spring.tcp.TcpServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TcpControllerTestConfiguration.class)
public class TcpControllerTest {

    @Autowired
    private TcpServer tcpServer;

    @Test
    public void testTcpServerIsCreated() {
        assertNotNull(tcpServer);
    }

}
