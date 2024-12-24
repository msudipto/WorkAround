package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for the WebSocket server.
 *
 * This class serves as the entry point for the Spring Boot application and
 * includes component scanning for WebSocket endpoints and other Spring
 * components within the specified base packages.
 *
 * The @SpringBootApplication annotation combines @Configuration, @EnableAutoConfiguration,
 * and @ComponentScan, enabling Spring Boot's autoconfiguration and component scanning features.
 **/

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo.websocket"})
public class WebSocketSpringbootApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * This method handles application startup and ensures that the
	 * Spring context is initialized properly.
	 *
	 * @param args command line arguments
	 **/

	public static void main(String[] args) {
		try {
			SpringApplication.run(WebSocketSpringbootApplication.class, args);
			System.out.println("WebSocket Server is running...");
		} catch (Exception e) {
			System.err.println("Error during application startup: " + e.getMessage());
			e.printStackTrace();
		}
	}
}