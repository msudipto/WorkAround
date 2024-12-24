package com.cs309.tutorial.course;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_registration") // Added quotes for table name
public class CourseRegistration {

    @Id
    @Column(name = "id") // Added quotes for column name
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id") // Added quotes for column name
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id") // Added quotes for column name
    private Course course;

    @Column(name = "registered_at") // Added quotes for column name
    private LocalDateTime registeredAt;

    @Column(name = "grade") // Added quotes for column name
    private int grade;

    // Default constructor
    public CourseRegistration() {}

    // Getter and setter methods with @SuppressWarnings("unused") to avoid unused warnings
    @SuppressWarnings("unused")
    public Student getStudent() {
        return student;
    }

    @SuppressWarnings("unused")
    public void setStudent(Student student) {
        this.student = student;
    }

    @SuppressWarnings("unused")
    public Course getCourse() {
        return course;
    }

    @SuppressWarnings("unused")
    public void setCourse(Course course) {
        this.course = course;
    }

    @SuppressWarnings("unused")
    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    @SuppressWarnings("unused")
    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    @SuppressWarnings("unused")
    public int getGrade() {
        return grade;
    }

    @SuppressWarnings("unused")
    public void setGrade(int grade) {
        this.grade = grade;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CourseRegistration other &&
                (id != null && id.equals(other.id));
    }
}