package com.segg3r.spring.tcp;

import java.net.InetAddress;

public interface TcpConnection {

    InetAddress getAddress();

    void send(Object objectToSend);

    void addListener(Listener listener);

    void start();

    void close();

    interface Listener {
        void onMessageReceived(TcpConnection connection, Object message);

        void onClientConnected(TcpConnection connection);

        void onClientDisconnected(TcpConnection connection);
    }

}
