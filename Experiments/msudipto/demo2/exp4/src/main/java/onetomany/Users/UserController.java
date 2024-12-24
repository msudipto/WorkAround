
package onetomany.Users;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;

/**
 * UserController class to manage REST API requests related to User entities.
 * 
 * Enhancements:
 * - Improved error handling and input validation.
 * - Refactored duplicate code into helper methods.
 * 
 * Author: Vivek Bengre
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    private static final String SUCCESS = "{\"message\":\"success\"}";
    private static final String FAILURE = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user) {
        validateUserInput(user);
        userRepository.save(user);
        return SUCCESS;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found for id: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    @PutMapping("/users/{userId}/laptops/{laptopId}")
    public String assignLaptopToUser(@PathVariable int userId, @PathVariable int laptopId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found for id: " + userId));
        Laptop laptop = laptopRepository.findById(laptopId).orElseThrow(() -> new RuntimeException("Laptop not found for id: " + laptopId));

        laptop.setUser(user);
        user.setLaptop(laptop);
        userRepository.save(user);
        return SUCCESS;
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return SUCCESS;
    }

    // Helper method to validate user input
    private void validateUserInput(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User's name and email must not be null");
        }
    }
}
