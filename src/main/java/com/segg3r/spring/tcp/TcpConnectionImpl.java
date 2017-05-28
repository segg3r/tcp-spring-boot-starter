package com.segg3r.spring.tcp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpConnectionImpl implements TcpConnection {

    private static final Logger LOG = LogManager.getLogger(TcpConnectionImpl.class);

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private List<Listener> listeners = new ArrayList<>();

    public TcpConnectionImpl(Socket socket) {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize TcpConnection for socket " + socket, e);
        }
    }

    @Override
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }

    @Override
    public void send(Serializable object) {
        try {
            out.writeObject(object);
        } catch (IOException e) {
            LOG.error("Could not send object to TcpConnection for socket " + socket, e);
        }
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Object object = in.readObject();

                    for (Listener listener : listeners) {
                        listener.onMessageReceived(this, object);
                    }
                } catch (Exception e) {
                    LOG.trace("Could not receive message from TcpConnection for socket " + socket, e);
                    for (Listener listener : listeners) {
                        listener.onClientDisconnected(this);
                    }
                    break;
                }
            }
        }).start();
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            LOG.error("Could not close TcpConnection for socket " + socket, e);
        }
    }

}
