package com.segg3r.spring.tcp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpConnectionImpl implements TcpConnection {

    private static final Logger LOG = LogManager.getLogger(TcpConnectionImpl.class);

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private List<Listener> listeners = new ArrayList<>();

    public TcpConnectionImpl(Socket socket) {
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize TcpConnection for socket " + socket, e);
        }
    }

    @Override
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }

    @Override
    public void send(Object objectToSend) {
        if (objectToSend instanceof byte[]) {
            byte[] data = (byte[]) objectToSend;
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                byte buf[] = new byte[64 * 1024];
                try {
                    int count = inputStream.read(buf);
                    if (count > 0) {
                        byte[] bytes = Arrays.copyOf(buf, count);
                        for (Listener listener : listeners) {
                            listener.onMessageReceived(this, bytes);
                        }
                    } else {
                        socket.close();
                        for (Listener listener : listeners) {
                            listener.onClientDisconnected(this);
                        }
                        break;
                    }
                } catch (IOException e) {
                    LOG.warn("Could not receive message from TcpConnection for socket " + socket, e);
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
