package com.cs309.tutorial.course;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_rating") // Use quotes to ensure correct table resolution
public class CourseRating {

    @EmbeddedId
    private CourseRatingKey id;

    @ManyToOne
    @MapsId("studentId") // Adjust the field name to match the embedded ID field name
    @JoinColumn(name = "student_id") // Use quotes for consistency
    private Student student;

    @ManyToOne
    @MapsId("courseId") // Adjust the field name to match the embedded ID field name
    @JoinColumn(name = "course_id") // Use quotes for consistency
    private Course course;

    @Column(name = "rating") // Use quotes for consistency
    private int rating;

    public CourseRating() {
    }

    @SuppressWarnings("unused")
    public int getRating() {
        return rating;
    }

    @SuppressWarnings("unused")
    public void setRating(int rating) {
        this.rating = rating;
    }

    @SuppressWarnings("unused")
    public CourseRatingKey getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public Student getStudent() {
        return student;
    }

    @SuppressWarnings("unused")
    public Course getCourse() {
        return course;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CourseRating other &&
                id != null &&
                id.equals(other.getId());
    }
}