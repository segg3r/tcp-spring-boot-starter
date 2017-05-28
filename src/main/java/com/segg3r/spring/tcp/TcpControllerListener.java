package com.segg3r.spring.tcp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TcpControllerListener implements TcpConnection.Listener {

    private static final Logger LOG = LogManager.getLogger(TcpControllerListener.class);

    private final Object bean;
    private final List<Method> receiveMethods;
    private final List<Method> connectMethods;
    private final List<Method> disconnectMethods;

    public TcpControllerListener(Object bean, List<Method> receiveMethods, List<Method> connectMethods, List<Method> disconnectMethods) {
        this.bean = bean;
        this.receiveMethods = receiveMethods;
        this.connectMethods = connectMethods;
        this.disconnectMethods = disconnectMethods;
    }

    @Override
    public void onMessageReceived(TcpConnection connection, Object message) {
        for (Method receiveMethod : receiveMethods) {
            Class<?> aClass = receiveMethod.getParameterTypes()[1];
            if (aClass.isAssignableFrom(message.getClass())) {
                try {
                    receiveMethod.invoke(bean, connection, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.error("Could not process received message " + message + " for connection " + connection.getAddress(), e);
                }
            }
        }
    }

    @Override
    public void onClientConnected(TcpConnection connection) {
        for (Method connectMethod : connectMethods) {
            try {
                connectMethod.invoke(bean, connection);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Could not process client connection for connection " + connection.getAddress(), e);
            }
        }
    }

    @Override
    public void onClientDisconnected(TcpConnection connection) {
        for (Method disconnectMethod : disconnectMethods) {
            try {
                disconnectMethod.invoke(bean, connection);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Could not process client disconnect for connection " + connection.getAddress(), e);
            }
        }
    }

}