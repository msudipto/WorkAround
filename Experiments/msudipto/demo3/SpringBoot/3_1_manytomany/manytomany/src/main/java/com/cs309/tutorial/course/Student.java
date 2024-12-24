package com.cs309.tutorial.course;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "student") // Added quotes to handle potential reserved word conflicts
public class Student {

    @Id
    @Column(name = "id") // Added quotes to handle potential reserved word conflicts
    private Long id;

    @ManyToMany
    @JoinTable(name = "course_like", // Added quotes to table name
            joinColumns = @JoinColumn(name = "student_id"), // Added quotes to column name
            inverseJoinColumns = @JoinColumn(name = "course_id")) // Added quotes to column name
    @SuppressWarnings("unused") // Added to suppress unused warnings
    private Set<Course> likedCourses;

    @OneToMany(mappedBy = "student")
    @SuppressWarnings("unused") // Added to suppress unused warnings
    private Set<CourseRating> ratings;

    @OneToMany(mappedBy = "student")
    @SuppressWarnings("unused") // Added to suppress unused warnings
    private Set<CourseRegistration> registrations;

    // Default constructor
    public Student() {}

    @SuppressWarnings("unused") // Added to suppress unused warnings
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused") // Added to suppress unused warnings
    public Set<Course> getLikedCourses() {
        return likedCourses;
    }

    @SuppressWarnings("unused") // Added to suppress unused warnings
    public Set<CourseRating> getRatings() {
        return ratings;
    }

    @SuppressWarnings("unused") // Added to suppress unused warnings
    public Set<CourseRegistration> getRegistrations() {
        return registrations;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Student other &&
                (id != null && id.equals(other.id));
    }
}