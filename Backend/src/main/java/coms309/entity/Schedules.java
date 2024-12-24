package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @NotBlank(message = "Event type is required")
    @Size(max = 255, message = "Event type must not exceed 255 characters")
    @Column(name = "event_type", length = 255)
    private String eventType; // e.g., "Shift", "Meeting"

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    @Column(name = "start_time", columnDefinition = "DATETIME(6)")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", columnDefinition = "DATETIME(6)")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Prevents recursion by indicating this side is the child
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference // Prevents recursion by indicating this side is the child
    private Projects project; // Added project relationship

    @Column(name = "created_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime updatedAt;

    @Column(name = "employee_assigned_to", nullable = true)
    private String employeeAssignedTo;

    @Column(name = "employer_assigned_to", nullable = true)
    private String employerAssignedTo;

    // Constructors
    public Schedules() {}

    public Schedules(Long scheduleId, String eventType, LocalDateTime startTime, LocalDateTime endTime, User user, Projects project, LocalDateTime createdAt, LocalDateTime updatedAt, String EmployeeAssignedTo, String EmployerAssignedTo) {
        this.scheduleId = scheduleId;
        this.eventType = eventType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.project = project;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.employeeAssignedTo = EmployeeAssignedTo;
        this.employerAssignedTo = EmployerAssignedTo;
    }

    // Getters and Setters
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
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

    // Utility methods for scheduling validations
    public boolean isConflict(Schedules other) {
        return (this.startTime.isBefore(other.getEndTime()) && this.endTime.isAfter(other.getStartTime()));
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