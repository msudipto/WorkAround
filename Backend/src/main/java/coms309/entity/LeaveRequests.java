package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity class representing leave requests made by employees.
 */
@Entity
@Getter
@Setter
@Table(name = "leave_requests")
public class LeaveRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long leaveId;

    @NotNull(message = "Start date cannot be null")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate = LocalDate.now();

    @Column(name = "approval_status", nullable = false)
    private String approvalStatus = "Pending"; // Default status

    @Column(name = "remarks_notes")
    private String remarksNotes;

    @NotNull(message = "Type of leave cannot be null")
    @Column(name = "type_of_leave", nullable = false)
    private String typeOfLeave;

    @NotNull(message = "Employee cannot be null")
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    @JsonBackReference // Child side
    private Employee employee;

    /**
     * Calculates the duration of the leave in days.
     * @return Number of days between startDate and endDate inclusive.
     */
    public long getLeaveDuration() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
