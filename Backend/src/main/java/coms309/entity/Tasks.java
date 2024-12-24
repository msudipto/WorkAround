
package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Task name is required")
    @Size(max = 100, message = "Task name must not exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Status is required")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    @Column(name = "status", nullable = false)
    private String status;  // e.g., "Assigned", "In Progress", "Completed"

    @Min(value = 0, message = "Progress must be between 0 and 100")
    @Max(value = 100, message = "Progress must be between 0 and 100")
    @Column(name = "progress")
    private Integer progress;  // Represented as a percentage (0-100)

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    @JsonManagedReference("project-task")
    private Projects project;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> assignedEmployees = new HashSet<>();


    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "employee_assigned_to", nullable = true)
    private String employeeAssignedTo;

    @Column(name = "employer_assigned_to", nullable = true)
    private String employerAssignedTo;

    @Column(name = "is_completed", nullable = true)
    private boolean isCompleted;

    // Constructors
    public Tasks() {}

    public Tasks(Long id, String name, String description, String status, Integer progress, Projects project, LocalDateTime createdAt, LocalDateTime updatedAt, String EmployeeAssignedTo, String EmployerAssignedTo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.progress = progress;
        this.project = project;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.employeeAssignedTo = EmployeeAssignedTo;
        this.employerAssignedTo = EmployerAssignedTo;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long taskId) {
        this.id = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projectId) {
        this.project = projectId;
    }

    public Set<User> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(Set<User> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
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

    public String getEmployeeAssignedTo() {
        return employeeAssignedTo;
    }

    public void setEmployeeAssignedTo(String employeeAssignedTo) {
        this.employeeAssignedTo = employeeAssignedTo;
    }

    public String getEmployerAssignedTo() {
        return employerAssignedTo;
    }

    public void setEmployerAssignedTo(String employerAssignedTo) {
        this.employerAssignedTo = employerAssignedTo;
    }

    // Utility methods to manage bidirectional relationship with User
    public void addEmployee(User employee) {
        assignedEmployees.add(employee);
        employee.getTasksSet().add(this);
    }

    public void removeEmployee(User employee) {
        assignedEmployees.remove(employee);
        employee.getTasksSet().remove(this);
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