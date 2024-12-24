package com.cs309.tutorial.course;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToMany(mappedBy = "likedCourses")
    @SuppressWarnings("unused")
    private Set<Student> likes;

    @OneToMany(mappedBy = "course")
    @SuppressWarnings("unused")
    private Set<CourseRating> ratings;

    @OneToMany(mappedBy = "course")
    @SuppressWarnings("unused")
    private Set<CourseRegistration> registrations;

    public Course() {}

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public Set<CourseRating> getRatings() {
        return ratings;
    }

    @SuppressWarnings("unused")
    public Set<CourseRegistration> getRegistrations() {
        return registrations;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Course other &&
                id != null &&
                id.equals(other.getId());
    }
}