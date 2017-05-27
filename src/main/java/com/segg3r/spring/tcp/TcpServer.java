package com.segg3r.spring.tcp;


import java.util.List;

public interface TcpServer {

    int getConnectionsCount();

    void setPort(int port);

    int getPort();

    void start();

    void stop();

    List<TcpConnection> getConnections();

    void addListener(TcpConnection.Listener listener);

}
