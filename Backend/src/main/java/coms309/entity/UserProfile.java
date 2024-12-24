package coms309.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * Entity class representing a user's profile.
 *
 * Improvements:
 * - Enhanced documentation for profile details.
 */
@Entity
@Getter
@Setter
@Table(name = "user_profiles")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name= "full_name", nullable = false)
    private String fullName;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = true)
    private UserType userType;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "job_title", nullable = true)
    private String jobTitle;

    @Column(name = "department", nullable = true)
    private String department;

    @Column(name = "date_of_hire", nullable = true)
    private Date dateOfHire;


    @Column(name= "time_worked", nullable = true)
    private int timeWorked;

    @Column(name = "next_shift", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextShift;

    @OneToOne(mappedBy = "userProfile")
    private Salary salary;

    @OneToOne(mappedBy = "userProfile")
    @JsonBackReference
    private Employer employer;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Employee> employees = new HashSet<>();



    public UserProfile(Long userId, String password, String username, String email ) {
        this.userId = Long.valueOf(userId);
        this.password = password;
        this.username = username;
        this.email = email;
        this.dateOfHire = new Date();
        this.timeWorked= timeWorked ;
        this.nextShift= new Date();
    }

    public UserProfile(){}
}

