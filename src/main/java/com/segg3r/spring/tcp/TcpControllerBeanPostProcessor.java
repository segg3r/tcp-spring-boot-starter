package com.segg3r.spring.tcp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TcpControllerBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> tcpControllerCache = new HashMap<>();

    @Autowired
    private TcpServer server;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(TcpController.class)) {
            tcpControllerCache.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (tcpControllerCache.containsKey(beanName)) {
            List<Method> receiveMethods = new ArrayList<>();
            List<Method> connectMethods = new ArrayList<>();
            List<Method> disconnectMethods = new ArrayList<>();

            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("receive") && method.getParameterCount() == 2
                        && method.getParameterTypes()[0] == TcpConnection.class) {
                    receiveMethods.add(method);
                } else if (method.getName().startsWith("connect") && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == TcpConnection.class) {
                    connectMethods.add(method);
                } else if (method.getName().startsWith("disconnect") && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == TcpConnection.class) {
                    disconnectMethods.add(method);
                }
            }

            TcpConnection.Listener listener = new TcpControllerListener(
                    bean, receiveMethods, connectMethods, disconnectMethods);
            server.addListener(listener);
        }

        return bean;
    }

}
