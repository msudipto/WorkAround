package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Entity class representing the availability of an employee.
 */
@Entity
@Getter
@Setter
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Long availabilityId;

    @NotNull(message = "Day of week cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DAY dayOfWeek;

    @NotNull(message = "Start time cannot be null")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
}

