package com.cs309.tutorial.course;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CourseRatingKey implements Serializable {

    @Column(name = "student_id") // Add quotes to handle naming conflicts or case sensitivity
    private Long studentId;

    @Column(name = "course_id") // Add quotes to handle naming conflicts or case sensitivity
    private Long courseId;

    public CourseRatingKey() {}

    @SuppressWarnings("unused")
    public Long getStudentId() {
        return studentId;
    }

    @SuppressWarnings("unused")
    public Long getCourseId() {
        return courseId;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CourseRatingKey other &&
                (courseId != null && courseId.equals(other.courseId)) &&
                (studentId != null && studentId.equals(other.studentId));
    }
}