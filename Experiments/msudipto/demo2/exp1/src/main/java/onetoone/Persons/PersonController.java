
package onetoone.Persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;

/**
 * PersonController class to manage REST API requests related to Person entities.
 * 
 * Enhancements:
 * - Improved error handling with more meaningful messages.
 * - Refactored duplicate code into helper methods.
 * - Added input validation to check for null values.
 * 
 * Author: Vivek Bengre
 */
@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    private static final String SUCCESS = "Operation successful";
    private static final String FAILURE = "Operation failed";

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/persons/{id}")
    public Person getPersonById(@PathVariable int id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found for id: " + id));
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        validatePersonInput(person);
        return personRepository.save(person);
    }

    @PutMapping("/persons/{id}")
    public Person updatePerson(@PathVariable int id, @RequestBody Person personDetails) {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found for id: " + id));
        person.setName(personDetails.getName());
        person.setEmail(personDetails.getEmail());
        return personRepository.save(person);
    }

    @PutMapping("/persons/{personId}/laptops/{laptopId}")
    public String assignLaptopToPerson(@PathVariable int personId, @PathVariable int laptopId) {
        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found for id: " + personId));
        Laptop laptop = laptopRepository.findById(laptopId).orElseThrow(() -> new RuntimeException("Laptop not found for id: " + laptopId));

        laptop.setPerson(person);
        person.setLaptop(laptop);
        personRepository.save(person);
        return SUCCESS;
    }

    @DeleteMapping("/persons/{id}")
    public String deletePerson(@PathVariable int id) {
        personRepository.deleteById(id);
        return SUCCESS;
    }

    // Helper method to validate person input
    private void validatePersonInput(Person person) {
        if (person.getName() == null || person.getEmail() == null) {
            throw new IllegalArgumentException("Person's name and email must not be null");
        }
    }
}
