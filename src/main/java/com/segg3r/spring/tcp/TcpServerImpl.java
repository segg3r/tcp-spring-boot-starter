package com.segg3r.spring.tcp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TcpServerImpl implements TcpServer, TcpConnection.Listener {

    private static final Logger LOG = LogManager.getLogger(TcpServerImpl.class);

    private int port;

    private ServerSocket serverSocket;
    private volatile boolean stopped;
    private List<TcpConnection> connections = new LinkedList<>();
    private List<TcpConnection.Listener> listeners = new ArrayList<>();

    @Override
    public int getConnectionsCount() {
        return connections.size();
    }

    @Override
    public void start() {
        try {
            LOG.info("Starting TcpServer on port " + port);
            this.serverSocket = new ServerSocket(port);
            LOG.info("Started TcpServer on port " + getPort());

            new Thread(() -> {
                while (!stopped) {
                    try {
                        Socket socket = serverSocket.accept();
                        if (socket.isConnected()) {
                            TcpConnectionImpl tcpConnection = new TcpConnectionImpl(socket);
                            tcpConnection.start();
                            tcpConnection.addListener(this);
                            onClientConnected(tcpConnection);
                        }
                    } catch (IOException e) {
                        LOG.error("Error in TcpServer cycle", e);
                    }
                }
            }).start();
        } catch (Exception e) {
            String message = "Could not start TcpServer";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public List<TcpConnection> getConnections() {
        return connections;
    }

    @Override
    public void addListener(TcpConnection.Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void onMessageReceived(TcpConnection connection, Object message) {
        LOG.trace("Received new message from " + connection.getAddress());
        LOG.trace("Received object: " + message.getClass().getCanonicalName() + ", toString: " + message.toString());
        for (TcpConnection.Listener listener : listeners) {
            listener.onMessageReceived(connection, message);
        }
    }

    @Override
    public void onClientConnected(TcpConnection connection) {
        LOG.info("Client connected: " + connection.getAddress() + ".");
        connections.add(connection);
        LOG.debug("Current connections count: " + connections.size());
        for (TcpConnection.Listener listener : listeners) {
            listener.onClientConnected(connection);
        }
    }

    @Override
    public void onClientDisconnected(TcpConnection connection) {
        LOG.info("Client disconnected: " + connection.getAddress() + ".");
        connections.remove(connection);
        LOG.debug("Current connections count: " + connections.size());
        for (TcpConnection.Listener listener : listeners) {
            listener.onClientDisconnected(connection);
        }
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int getPort() {
        return serverSocket != null
                ? serverSocket.getLocalPort()
                : port;
    }

}

