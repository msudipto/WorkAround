package onetomany.Phones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneController {

    @Autowired
    PhoneRepository phoneRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/phones")
    List<Phone> getAllPhones(){
        return phoneRepository.findAll();
    }

    @GetMapping(path = "/phones/{id}")
    Phone getPhoneById( @PathVariable int id){
        return phoneRepository.findById(id).orElseThrow(() -> new RuntimeException("Phone not found for id " + id));
    }

    @PutMapping(path = "/phones/{id}")
    public Phone updatePhone(@PathVariable int id, @RequestBody Phone request) {
        // Retrieve the phone from the repository or throw an exception if not found
        Phone phone = phoneRepository.findById(id).orElseThrow(() -> new RuntimeException("Phone not found for id " + id));

        // Update the phone's properties with the incoming request data
        phone.setName(request.getName());
        phone.setManufacturer(request.getManufacturer());
        phone.setCameraQuality(request.getCameraQuality());
        phone.setBattery(request.getBattery());
        phone.setPrice(request.getPrice());

        // Save the updated phone object
        phoneRepository.save(phone);

        // Return the updated phone object
        return phone;
    }
      
}
