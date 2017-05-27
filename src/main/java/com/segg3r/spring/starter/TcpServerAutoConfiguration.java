package com.segg3r.spring.starter;

import com.segg3r.spring.tcp.TcpServer;
import com.segg3r.spring.tcp.TcpControllerBeanPostProcessor;
import com.segg3r.spring.tcp.TcpServerImpl;
import com.segg3r.spring.tcp.TcpServerAutoStarterApplicationListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TcpServerProperties.class)
@ConditionalOnProperty(prefix = "tcp.server", name = {"port"})
public class TcpServerAutoConfiguration {

    @Bean
    TcpServerAutoStarterApplicationListener tcpServerAutoStarterApplicationListener() {
        return new TcpServerAutoStarterApplicationListener();
    }

    @Bean
    TcpControllerBeanPostProcessor tcpControllerBeanPostProcessor() {
        return new TcpControllerBeanPostProcessor();
    }

    @Bean
    TcpServer tcpServer() {
        return new TcpServerImpl();
    }

}
