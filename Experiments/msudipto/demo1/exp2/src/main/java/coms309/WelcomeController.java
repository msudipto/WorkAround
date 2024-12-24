package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned.
 * This version can be extended to include dynamic content.
 * 
 * @author Vivek Bengre
 */

@RestController
class WelcomeController {

    /**
     * Welcome endpoint that returns a simple welcome message.
     * @return A static welcome message for users visiting the root URL.
     */
    @GetMapping("/")
    public String welcome() {
        // Returning a simple message to welcome users
        return "Hello and welcome to COMS 309 - An enhanced Spring Boot application!";
    }
}