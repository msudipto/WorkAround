
package onetoone.Persons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import onetoone.Laptops.Laptop;

/**
 * Person entity class representing a person with attributes like name and email.
 * 
 * Enhancements:
 * - Improved variable names and comments for better clarity.
 * - Added validation to ensure name and email are non-null.
 * 
 * Author: Vivek Bengre
 */
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;

    @OneToOne(mappedBy = "person")
    private Laptop laptop;

    public Person() {}

    public Person(String name, String email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and email cannot be null.");
        }
        this.name = name;
        this.email = email;
    }

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

    public Person orElseThrow(Object o) { return null; }
}
