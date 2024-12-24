package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Entity class representing a time log entry for an employee.
 *
 * Improvements:
 * - Added validation annotations to enforce data integrity.
 * - Enhanced documentation for field-level relationships and time tracking.
 */
@Entity
@Getter
@Setter
@Table(name = "time_log")
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_log_id")
    private Long timeLogId;

    @NotNull(message = "Log date cannot be null")
    @Temporal(TemporalType.DATE)
    @Column(name = "log_date", nullable = false)
    private Date logDate;

    @Column(name = "clock_in_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clockInTime;

    @Column(name = "clock_out_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clockOutTime;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;


}

