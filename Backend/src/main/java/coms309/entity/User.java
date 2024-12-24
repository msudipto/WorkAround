package coms309.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Role is required")
    @Size(max = 50, message = "Role must not exceed 50 characters")
    private String role; // e.g., "Admin", "Employer", "Employee"

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Schedules> schedulesList = new ArrayList<>();

    @ManyToMany(mappedBy = "assignedEmployees")
    @JsonManagedReference
    private Set<Tasks> tasksSet = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Schedules> getSchedulesList() {
        return schedulesList;
    }

    public void setSchedulesList(List<Schedules> schedulesList) {
        this.schedulesList = schedulesList;
    }

    public Set<Tasks> getTasksSet() {
        return tasksSet;
    }

    public void setTasksSet(Set<Tasks> tasksSet) {
        this.tasksSet = tasksSet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods to manage bidirectional relationship with Schedules
    public void addSchedule(Schedules schedule) {
        schedulesList.add(schedule);
        schedule.setUser(this);
    }

    public void removeSchedule(Schedules schedule) {
        schedulesList.remove(schedule);
        schedule.setUser(null);
    }

    // Utility methods to manage bidirectional relationship with Tasks
    public void addTask(Tasks task) {
        tasksSet.add(task);
        task.getAssignedEmployees().add(this);
    }

    public void removeTask(Tasks task) {
        tasksSet.remove(task);
        task.getAssignedEmployees().remove(this);
    }

    // Lifecycle hooks to update timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}