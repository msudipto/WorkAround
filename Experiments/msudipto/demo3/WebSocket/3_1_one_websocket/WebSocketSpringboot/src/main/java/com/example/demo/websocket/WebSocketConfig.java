package com.example.demo.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for WebSocket server endpoints.
 *
 * This class registers WebSocket endpoints with Spring so that requests to ws://
 * can be handled by annotated WebSocket handlers, like the /chat endpoint in ChatServer.
 * The ServerEndpointExporter bean will automatically register any @ServerEndpoint
 * annotated beans with Spring's WebSocket support.
 **/

@Configuration
public class WebSocketConfig {

    // Logger for tracking configuration setup
    private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    // Property to enable or disable WebSocket support
    @Value("${websocket.enabled:true}")
    private boolean isWebSocketEnabled;

    /**
     * Provides a ServerEndpointExporter bean to register WebSocket endpoints.
     *
     * This bean is required to enable WebSocket support in Spring applications
     * using an embedded servlet container like Tomcat or Jetty.
     *
     * @return the ServerEndpointExporter bean
     **/

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        if (isWebSocketEnabled) {
            logger.info("[WebSocketConfig] WebSocket support enabled. Registering endpoints...");
            return new ServerEndpointExporter();
        } else {
            logger.warn("[WebSocketConfig] WebSocket support is disabled. No endpoints will be registered.");
            return null;
        }
    }
}