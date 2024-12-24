package org.springframework.samples.petclinic.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        logger.info("Welcome page accessed");
        String welcomeMessage = "Welcome to Petclinic</br> Go to localhost:8080/owners/create to create new data </br>";
        return new ResponseEntity<>(welcomeMessage, HttpStatus.OK);
    }
}