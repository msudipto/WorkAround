
package onetoone.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import onetoone.Laptops.Laptop;

/**
 * User entity class representing a system user with details such as name and email.
 * 
 * Enhancements:
 * - Improved variable names and readability.
 * - Added validation for name and email to ensure they are non-null.
 * 
 * Author: Vivek Bengre
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;

    @OneToOne(mappedBy = "user")
    private Laptop laptop;

    public User() {}

    public User(String name, String email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and email cannot be null.");
        }
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public User orElseThrow(Object o) { return null; }
}
