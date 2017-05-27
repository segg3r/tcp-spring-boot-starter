package com.segg3r.spring;

import com.segg3r.spring.tcp.TcpConnection;
import com.segg3r.spring.tcp.TcpController;

@TcpController
public class TestTcpController {

    public void receiveData(TcpConnection connection, byte[] data) {
        String answer = new String(data);
        connection.send(answer.toUpperCase().getBytes());
    }

    public void connect(TcpConnection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());
    }

    public void disconnect(TcpConnection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }

}