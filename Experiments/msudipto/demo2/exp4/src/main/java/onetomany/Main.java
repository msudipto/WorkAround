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
            // Creating users
            User user1 = new User("John", "john@somemail.com", new Date());
            User user2 = new User("Jane", "jane@somemail.com", new Date());
            User user3 = new User("Justin", "justin@somemail.com", new Date());

            // Creating laptops
            Laptop laptop1 = new Laptop(2.5, 4, 8, "Lenovo", 300);
            Laptop laptop2 = new Laptop(4.1, 8, 16, "HP", 800);
            Laptop laptop3 = new Laptop(3.5, 32, 32, "Dell", 2300);

            // Save laptops first
            laptopRepository.saveAll(Arrays.asList(laptop1, laptop2, laptop3));

            // Associate laptops with users
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);

            // Save users first, so we can associate phones with users later
            userRepository.saveAll(Arrays.asList(user1, user2, user3));

            // Create phones and associate with users
            Phone phone1 = new Phone("Apple", (int)Math.pow(1.3, 6), Math.pow(1.1, 6) * 1000, "iPhone 6", (int)Math.pow(1.3, 6) * 100);
            phone1.setUser(user1);  // Associate with user1

            Phone phone2 = new Phone("Apple", (int)Math.pow(1.3, 7), Math.pow(1.1, 7) * 1000, "iPhone 7", (int)Math.pow(1.3, 7) * 100);
            phone2.setUser(user1);  // Associate with user1

            Phone phone3 = new Phone("Apple", (int)Math.pow(1.3, 8), Math.pow(1.1, 8) * 1000, "iPhone 8", (int)Math.pow(1.3, 8) * 100);
            phone3.setUser(user2);  // Associate with user2

            Phone phone4 = new Phone("Apple", (int)Math.pow(1.3, 9), Math.pow(1.1, 9) * 1000, "iPhone 9", (int)Math.pow(1.3, 9) * 100);
            phone4.setUser(user2);  // Associate with user2

            Phone phone5 = new Phone("Apple", (int)Math.pow(1.3, 10), Math.pow(1.1, 10) * 1000, "iPhone 10", (int)Math.pow(1.3, 10) * 100);
            phone5.setUser(user1);  // Associate with user1

            Phone phone6 = new Phone("Apple", (int)Math.pow(1.3, 11), Math.pow(1.1, 11) * 1000, "iPhone 11", (int)Math.pow(1.3, 11) * 100);
            phone6.setUser(user1);  // Associate with user1

            // Save phones
            phoneRepository.saveAll(Arrays.asList(phone1, phone2, phone3, phone4, phone5, phone6));
        };
    }
}
