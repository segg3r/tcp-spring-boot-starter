package com.segg3r.spring.tcp;

import com.segg3r.spring.starter.TcpServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class TcpServerAutoStarterApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TcpServerProperties properties;
    @Autowired
    private TcpServer server;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        server.setPort(properties.getPort());
        server.start();
    }

}
