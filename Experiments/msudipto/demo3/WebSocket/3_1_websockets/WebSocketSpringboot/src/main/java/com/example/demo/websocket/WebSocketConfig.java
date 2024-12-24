package com.example.demo.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebSocket configuration class.
 *
 * This class registers WebSocket endpoints with Spring to ensure that
 * WebSocket connections can be handled. It also configures cross-origin
 * support to allow connections from different origins and adds basic
 * security and connection management settings.
 **/

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebMvcConfigurer {

    /**
     * Registers the ServerEndpointExporter bean.
     *
     * This bean is necessary to enable WebSocket endpoint handling
     * in a Spring Boot application. It scans and registers all endpoints
     * defined with the @ServerEndpoint annotation.
     *
     * @return the configured ServerEndpointExporter bean
     **/

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * Configure CORS settings to allow WebSocket connections from
     * different origins. Adjust the allowed origins as per your requirements.
     *
     * @param registry the CORS registry
     **/

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Adjust this to specific origins as needed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600); // Cache pre-flight response for 1 hour
    }
}