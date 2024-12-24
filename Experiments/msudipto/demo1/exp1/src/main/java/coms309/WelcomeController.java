package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class WelcomeController {

    /**
     * Home endpoint that returns a personalized message.
     * Now includes the option to customize the message and simulate text formatting.
     * @param name The name of the person visiting the application.
     * @return A greeting message customized for the visitor.
     */
    @RequestMapping("/")
    public String getGreetingMessage(@RequestParam(value = "name", defaultValue = "Guest") String name) {
        // Customize the name: first letter capitalized, rest lowercased
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        // Simulate message customization with simple text formatting hints (bold and italic placeholders)
        return "<b>Hello</b>, <i>" + formattedName + "</i>! Welcome to the Spring Boot application.";
    }

    /**
     * Status endpoint to check the application's health status.
     * @return A message that indicates the application is running fine.
     */
    @RequestMapping("/status")
    public String getAppStatus() {
        return "<span style='color:green;'>The application is running smoothly with enhanced features!</span>";
    }

    /**
     * Information endpoint that provides more details about the application.
     * @return A brief description of the application's capabilities.
     */
    @RequestMapping("/info")
    public String getAppInfo() {
        return "This application has been modified to serve customized endpoints with more meaningful code.";
    }

    /**
     * New feature: welcome message with additional customization based on user preference.
     * @param name Name of the visitor
     * @param isFormal Whether the greeting should be formal or casual
     * @return A personalized message based on user preference.
     */
    @RequestMapping("/greet")
    public String getCustomGreeting(@RequestParam(value = "name", defaultValue = "Guest") String name,
                                    @RequestParam(value = "isFormal", defaultValue = "false") boolean isFormal) {
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        if (isFormal) {
            return "Good day, " + formattedName + ". It is a pleasure to welcome you.";
        } else {
            return "Hey" + formattedName + "! Welcome to the app, enjoy your stay!";
        }
    }

    public static void main(String[] args) {
        // Run the Spring Boot application with the enhanced WelcomeController
        SpringApplication.run(WelcomeController.class, args);
    }
}