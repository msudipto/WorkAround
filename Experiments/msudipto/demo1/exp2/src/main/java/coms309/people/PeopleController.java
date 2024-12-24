package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

/**
 * Controller to manage CRUDL operations for people.
 * Uniquely modified with additional validation and features.
 */

@RestController
public class PeopleController {

    // Store people using a HashMap where firstName is the key
    private HashMap<String, Person> peopleDatabase = new HashMap<>();

    /**
     * List all people in the database.
     * @return HashMap of all persons in JSON format.
     */
    @GetMapping("/people")
    public HashMap<String, Person> getAllPeople() {
        return peopleDatabase;
    }

    /**
     * Create a new person and add them to the database.
     * Prevents duplicate first names.
     * @param person Person object in JSON format.
     * @return Success or error message.
     */
    @PostMapping("/people")
    public String addPerson(@RequestBody Person person) {
        if (peopleDatabase.containsKey(person.getFirstName())) {
            return "Error: A person with the first name '" + person.getFirstName() + "' already exists.";
        }
        peopleDatabase.put(person.getFirstName(), person);
        return "New person '" + person.getFirstName() + "' successfully added.";
    }

    /**
     * Get details of a specific person by their first name.
     * @param firstName The first name of the person.
     * @return Person object or error message.
     */
    @GetMapping("/people/{firstName}")
    public Person getPerson(@PathVariable String firstName) {
        return peopleDatabase.getOrDefault(firstName, null);
    }

    /**
     * Update the details of an existing person.
     * @param firstName The first name of the person to be updated.
     * @param updatedPerson Updated Person object.
     * @return Updated Person object.
     */
    @PutMapping("/people/{firstName}")
    public Person updatePerson(@PathVariable String firstName, @RequestBody Person updatedPerson) {
        peopleDatabase.replace(firstName, updatedPerson);
        return peopleDatabase.get(firstName);
    }

    /**
     * Delete a person by their first name.
     * @param firstName The first name of the person to be deleted.
     * @return Updated list of people in the database.
     */
    @DeleteMapping("/people/{firstName}")
    public HashMap<String, Person> deletePerson(@PathVariable String firstName) {
        peopleDatabase.remove(firstName);
        return peopleDatabase;
    }

    /**
     * Search for people by last name.
     * @param lastName The last name to search for.
     * @return HashMap of people with matching last names.
     */
    @GetMapping("/people/search/{lastName}")
    public HashMap<String, Person> searchByLastName(@PathVariable String lastName) {
        HashMap<String, Person> result = new HashMap<>();
        for (Person person : peopleDatabase.values()) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                result.put(person.getFirstName(), person);
            }
        }
        return result;
    }
}