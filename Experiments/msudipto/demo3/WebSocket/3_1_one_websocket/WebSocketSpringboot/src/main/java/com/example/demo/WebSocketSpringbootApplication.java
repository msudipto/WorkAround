package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main application class for the WebSocket server.
 *
 * This class uses the `@ComponentScan` annotation to specify the base package
 * for component scanning. It scans the "com.example.demo.websocket" package
 * and its subpackages for Spring components, such as WebSocket endpoints and
 * controllers.
 *
 * It also handles application startup logging and provides support for graceful
 * shutdown to ensure proper resource cleanup.
 **/

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo.websocket"})
public class WebSocketSpringbootApplication {

	// Logger for application startup
	private static final Logger logger = LoggerFactory.getLogger(WebSocketSpringbootApplication.class);

	public static void main(String[] args) {
		try {
			// Log application startup
			logger.info("[WebSocketSpringbootApplication] Starting WebSocket server application...");
			SpringApplication.run(WebSocketSpringbootApplication.class, args);
			logger.info("[WebSocketSpringbootApplication] Application started successfully.");
		} catch (Exception e) {
			logger.error("[WebSocketSpringbootApplication] Error during startup: " + e.getMessage(), e);
		}

		// Add shutdown hook for graceful termination
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("[WebSocketSpringbootApplication] Application is shutting down...");
			// Additional cleanup tasks can be performed here
			logger.info("[WebSocketSpringbootApplication] Shutdown complete.");
		}));
	}
}