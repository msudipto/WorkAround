package onetomany.Phones;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Users.User;

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "camera_quality", nullable = false)
    private double cameraQuality;

    @Column(name = "battery", nullable = false)
    private double battery;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Ensure it’s not null as it references a user
    @JsonIgnore
    private User user;

    // =============================== Constructors ================================== //

    public Phone() {}

    public Phone(String manufacturer, double cameraQuality, double battery, String name, int price) {
        this.manufacturer = manufacturer;
        this.cameraQuality = cameraQuality;
        this.battery = battery;
        this.name = name;
        this.price = price;
    }

    // =============================== Getters and Setters ================================== //

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getCameraQuality() {
        return cameraQuality;
    }

    public void setCameraQuality(double cameraQuality) {
        this.cameraQuality = cameraQuality;
    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}