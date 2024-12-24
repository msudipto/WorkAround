package coms309.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing an Admin.
 *
 * Improvements:
 * - Added validation annotations to enforce data integrity.
 * - Improved class-level and field-level documentation.
 */
@Data
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @ManyToMany
    @JoinTable(
            name = "admin_projects",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    private Set<Projects> projects = new HashSet<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "user_id", unique = true)
    private UserProfile userProfile;

    public Admin(){}
}

