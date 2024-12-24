
package onetomany.Laptops;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Users.User;

/**
 * Laptop entity class representing a laptop with various specifications.
 * 
 * Enhancements:
 * - Improved variable naming and readability.
 * - Added validation for fields like CPU clock speed and RAM.
 * 
 * Author: Vivek Bengre
 */
@Entity
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double cpuClock; // CPU clock speed in GHz
    private int cpuCores;    // Number of CPU cores
    private int ram;         // RAM in GB
    private String manufacturer;
    private int cost;

    @OneToOne
    @JsonIgnore
    private User user;

    public Laptop(double cpuClock, int cpuCores, int ram, String manufacturer, int cost) {
        if (cpuClock <= 0 || ram <= 0) {
            throw new IllegalArgumentException("Invalid specs: CPU clock and RAM must be positive.");
        }
        this.cpuClock = cpuClock;
        this.cpuCores = cpuCores;
        this.ram = ram;
        this.manufacturer = manufacturer;
        this.cost = cost;
    }

    public Laptop() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCpuClock() {
        return cpuClock;
    }

    public void setCpuClock(double cpuClock) {
        if (cpuClock <= 0) {
            throw new IllegalArgumentException("CPU clock speed must be positive.");
        }
        this.cpuClock = cpuClock;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        if (ram <= 0) {
            throw new IllegalArgumentException("RAM must be positive.");
        }
        this.ram = ram;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
