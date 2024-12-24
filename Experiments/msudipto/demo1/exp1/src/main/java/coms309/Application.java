package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Custom Spring Boot Application for a unique project.
 * This application serves as a customizable backend service
 * with enhanced features and more meaningful method/variable names.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Starts the customized Spring Boot application with additional endpoints
        SpringApplication.run(Application.class, args);
    }
}