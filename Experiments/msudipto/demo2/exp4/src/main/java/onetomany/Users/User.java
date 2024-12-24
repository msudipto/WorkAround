package onetomany.Users;

import jakarta.persistence.*;
import java.util.Date;
import onetomany.Laptops.Laptop;
import java.util.List;
import java.util.ArrayList;
import onetomany.Phones.Phone;

/**
 * User entity class representing a system user with details such as name, email, and created date.
 *
 * Enhancements:
 * - Managed bi-directional relationships between User and Phone.
 * - Added validation for name and email.
 * - Automatically sets the created date if not provided.
 * - Provided safe methods for adding and removing phones.
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(mappedBy = "user")
    private Laptop laptop;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    // Default constructor
    public User() {
        this.createdDate = new Date(); // Initialize with the current date
    }

    // Parameterized constructor
    public User(String name, String email, Date createdDate) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and email cannot be null.");
        }
        this.name = name;
        this.email = email;
        this.createdDate = createdDate != null ? createdDate : new Date(); // Use provided date or current date
    }

    // ========================== Getters and Setters ========================= //

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    // ======================= Relationship Management ======================= //

    /**
     * Adds a phone to the user's phone list and ensures the phone is associated with the user.
     *
     * @param phone the phone to add
     */
    public void addPhone(Phone phone) {
        if (phone != null) {
            this.phones.add(phone);
            phone.setUser(this); // Ensure the phone references this user
        }
    }

    /**
     * Removes a phone from the user's phone list and ensures the phone's user is set to null.
     *
     * @param phone the phone to remove
     */
    public void removePhone(Phone phone) {
        if (phone != null) {
            this.phones.remove(phone);
            phone.setUser(null); // Nullify the relationship
        }
    }
}