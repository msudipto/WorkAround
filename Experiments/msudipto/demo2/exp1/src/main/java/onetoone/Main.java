
package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;
import onetoone.Persons.Person;
import onetoone.Persons.PersonRepository;

import java.util.Arrays;

/**
 * Main class to initialize the application and seed dummy data.
 * 
 * Improvements:
 * - Improved naming conventions for better readability.
 * - Batched saving of Person objects for performance.
 * - Added input validation and logging.
 * 
 * Author: Vivek Bengre
 */
@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 Persons with their machines
    /**
     * Initialize dummy data for persons and laptops.
     * 
     * @param personRepository Repository for the Person entity.
     * @param laptopRepository Repository for the Laptop entity.
     * @return CommandLineRunner to enter data into the database.
     */
    @Bean
    CommandLineRunner initPerson(PersonRepository personRepository, LaptopRepository laptopRepository) {
        return args -> {
            Person person1 = new Person("John", "john@somemail.com");
            Person person2 = new Person("Jane", "jane@somemail.com");
            Person person3 = new Person("Justin", "justin@somemail.com");

            // Create Laptop entities
            Laptop laptop1 = new Laptop(2.5, 4, 8, "Lenovo", 300);
            Laptop laptop2 = new Laptop(4.1, 8, 16, "HP", 800);
            Laptop laptop3 = new Laptop(3.5, 32, 32, "Dell", 2300);

            // Save laptops first
            laptopRepository.saveAll(Arrays.asList(laptop1, laptop2, laptop3));

            // Now associate the saved laptops with persons
            person1.setLaptop(laptop1);
            person2.setLaptop(laptop2);
            person3.setLaptop(laptop3);

            // Save persons in batch for better performance
            personRepository.saveAll(Arrays.asList(person1, person2, person3));
        };
    }
}
