package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample Spring Boot Application with enhanced structure.
 * 
 * This serves as the entry point for the Spring Boot application.
 * Modifications include enhanced exception handling setup.
 * 
 * @author Vivek Bengre
 */

@SpringBootApplication
public class Application {
    
    public static void main(String[] args) throws Exception {
        // Run the Spring Boot application
        SpringApplication.run(Application.class, args);
    }

}