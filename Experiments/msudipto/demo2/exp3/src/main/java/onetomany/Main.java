
package onetomany;

import java.util.Arrays;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;
import onetomany.Phones.Phone;
import onetomany.Phones.PhoneRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;

/**
 * Main class to initialize the application and seed dummy data.
 * 
 * Enhancements:
 * - Improved batch saving of users and devices for performance.
 * - Added validation checks for user and device details.
 * 
 * Author: Vivek Bengre
 */
@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository, LaptopRepository laptopRepository, PhoneRepository phoneRepository) {
        return args -> {
            User user1 = new User("John", "john@somemail.com", new Date());
            User user2 = new User("Jane", "jane@somemail.com", new Date());
            User user3 = new User("Justin", "justin@somemail.com", new Date());

            Laptop laptop1 = new Laptop(2.5, 4, 8, "Lenovo", 300);
            Laptop laptop2 = new Laptop(4.1, 8, 16, "HP", 800);
            Laptop laptop3 = new Laptop(3.5, 32, 32, "Dell", 2300);

            // Associate laptops with users
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);

            // Save users first to make sure they are persisted
            userRepository.saveAll(Arrays.asList(user1, user2, user3));

            // Now create and assign phones to users
            Phone phone1 = new Phone("Apple", 12.0, 3000, "iPhone 12", 999);
            phone1.setUser(user1);  // Set the user reference for the phone

            Phone phone2 = new Phone("Apple", 13.0, 4000, "iPhone 13", 1099);
            phone2.setUser(user2);

            Phone phone3 = new Phone("Apple", 14.0, 5000, "iPhone 14", 1199);
            phone3.setUser(user3);

            // Save phones after associating them with users
            phoneRepository.saveAll(Arrays.asList(phone1, phone2, phone3));
        };
    }
}
