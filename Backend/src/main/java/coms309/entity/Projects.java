package coms309.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Entity class representing a project.
 *
 * Improvements:
 * - Added validation annotations for data integrity.
 * - Enhanced documentation for relationships with employees and employers.
 */
@Entity
@Getter
@Setter
@Table(name = "projects")
public class Projects implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;


    @NotNull(message = "Project name cannot be null")
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @NotNull(message = "Username cannot be null")
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull(message = "Project description cannot be null")
    @Column(name = "project_description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "due_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime dueDate;

    @ManyToMany(mappedBy = "projects")
    @JsonBackReference("employer-project")
    private Set<Employer> employers = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    private Set<Admin> admins = new HashSet<>();

    @OneToMany(mappedBy = "projects", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("project-employee")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("project-task")
    private Set<Tasks> tasks = new HashSet<>();


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    public Projects(){}

    public Projects(Long projectId, String projectName, String username, String description, String status, String priority, LocalDateTime dueDate, LocalDateTime startDate, LocalDateTime endDate ) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.username = username;
        this.description= description;
        this.status= status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setName(String name) {
        this.projectName = name;
    }

    public String getName() {
        return projectName;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }
}