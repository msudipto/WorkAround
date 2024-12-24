package onetomany.Phones;

import java.util.List;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PhoneController {

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    UserRepository userRepository; // Add this line to inject UserRepository

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/phones")
    List<Phone> getAllPhones(){
        return phoneRepository.findAll();
    }

    @GetMapping(path = "/phones/{id}")
    Phone getPhoneById(@PathVariable int id){
        return phoneRepository.findById(id).get();
    }

    @PostMapping(path = "/phones")
    public String createPhone(@RequestBody Phone phone, @RequestParam int userId) {
        if (phone == null) {
            return failure;
        }

        // Fetch the user by userId and associate it with the phone
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        phone.setUser(user);  // Set the user in the phone

        // Save the phone with the associated user
        phoneRepository.save(phone);

        return success;
    }

    @PutMapping(path = "/phones/{id}")
    public Phone updatePhone(@PathVariable int id, @RequestBody Phone request) {
        // Use Optional to handle potential null and throw an exception if not found
        Phone existingPhone = phoneRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone not found"));

        // Update the fields of the existing phone with the data from the request
        existingPhone.setName(request.getName());
        existingPhone.setManufacturer(request.getManufacturer());
        existingPhone.setBattery(request.getBattery());
        existingPhone.setCameraQuality(request.getCameraQuality());
        existingPhone.setPrice(request.getPrice());

        // Save the updated phone
        return phoneRepository.save(existingPhone);
    }
}
