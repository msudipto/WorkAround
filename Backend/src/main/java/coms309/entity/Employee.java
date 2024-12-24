package coms309.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity class representing an Employee.
 */
@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

   @OneToMany(mappedBy="employee", cascade = CascadeType.ALL)
   @JsonManagedReference
   private List<LeaveRequests> leaveRequestsList = new ArrayList<>();

   @OneToMany(mappedBy = "employee" , cascade = CascadeType.ALL)
   @JsonManagedReference
   private List<TimeLog> timeLogs= new ArrayList<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id", unique = true)
    private UserProfile userProfile;

    @ManyToMany
    @JoinTable(
            name = "employer_projects",
            joinColumns = @JoinColumn(name = "employer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonManagedReference("employer-project")
    private Set<Projects> projects = new HashSet<>();

    public Employee(){}
}

