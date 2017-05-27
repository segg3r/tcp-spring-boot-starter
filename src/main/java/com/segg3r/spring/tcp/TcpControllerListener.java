package com.segg3r.spring.tcp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TcpControllerListener implements TcpConnection.Listener {

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
            if (message.getClass().isAssignableFrom(aClass)) {
                try {
                    receiveMethod.invoke(bean, connection, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientDisconnected(TcpConnection connection) {
        for (Method disconnectMethod : disconnectMethods) {
            try {
                disconnectMethod.invoke(bean, connection);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}