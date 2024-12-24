package coms309.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.SpringConfigurator;

@Configuration
@EnableWebSocketMessageBroker
public class ChatSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers the ServerEndpointExporter bean, which is required for WebSocket
     * endpoint configuration. This bean is only needed when running in an
     * embedded server (e.g., Spring Boot).
     *
     * @return ServerEndpointExporter instance if running in an embedded server,
     *         null otherwise.
     **/
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configure a simple in-memory message broker
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint for STOMP connections
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }
}
