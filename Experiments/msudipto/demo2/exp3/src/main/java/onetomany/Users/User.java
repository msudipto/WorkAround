package onetomany.Users;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import onetomany.Laptops.Laptop;
import onetomany.Phones.Phone;

/**
 * User entity class representing a system user with details such as name, email, and associated devices (laptop and phones).
 *
 * Enhancements:
 * - Proper mapping for relationships (One-to-One with Laptop, One-to-Many with Phones).
 * - Added validation for fields like name, email, and phone association.
 * - Ensured proper cascading and fetch strategies for better performance and data integrity.
 *
 * Author: Vivek Bengre
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Laptop laptop;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Phone> phones = new ArrayList<>();

    // =============================== Constructors ================================== //

    public User() {}

    public User(String name, String email, Date createdDate) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        this.name = name;
        this.email = email;
        this.createdDate = createdDate != null ? createdDate : new Date();
    }

    // =============================== Getters and Setters ================================== //

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
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
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

    public void addPhone(Phone phone) {
        if (phone != null) {
            this.phones.add(phone);
            phone.setUser(this);  // Set the relationship properly on the Phone side
        }
    }

    public void removePhone(Phone phone) {
        if (phone != null) {
            this.phones.remove(phone);
            phone.setUser(null);  // Remove the relationship properly
        }
    }
}