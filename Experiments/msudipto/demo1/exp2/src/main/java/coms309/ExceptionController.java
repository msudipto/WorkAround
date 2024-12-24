package coms309;

/**
 * Controller used to showcase what happens when an exception is thrown.
 * This version includes a more descriptive error message when an exception occurs.
 * 
 * @author Vivek Bengre
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExceptionController {

    /**
     * Trigger an exception deliberately to showcase Spring Boot's error handling mechanism.
     * @return No return value as this method always throws an exception.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/oops")
    public String triggerException() {
        // Throwing a runtime exception to simulate an error
        throw new RuntimeException("Oops! Something went wrong. This is a deliberate exception for testing purposes.");
    }

}