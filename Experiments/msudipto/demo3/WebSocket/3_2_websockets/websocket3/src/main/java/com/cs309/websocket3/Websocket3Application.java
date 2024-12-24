package com.cs309.websocket3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class Websocket3Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		// Start the Spring Boot application with error handling
		try {
			ConfigurableApplicationContext context = SpringApplication.run(Websocket3Application.class, args);
			Environment env = context.getEnvironment();
			System.out.println("Application '" + env.getProperty("spring.application.name", "Websocket3")
					+ "' is running on port: " + env.getProperty("server.port", "8080"));
		} catch (Exception e) {
			System.err.println("Application failed to start due to an error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Websocket3Application.class);
	}

	/**
	 * Custom Spring Boot Banner
	 **/

	static {
		SpringApplication application = new SpringApplication(Websocket3Application.class);
		application.setBanner((environment, sourceClass, out) -> printCustomBanner(out));
	}

	private static void printCustomBanner(PrintStream out) {
		out.println("***********************************");
		out.println("    Welcome to Websocket App!      ");
		out.println("***********************************");
	}
}