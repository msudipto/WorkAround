package coms309.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object for LeaveRequests entity.
 */
@Getter
@Setter
public class LeaveRequestDTO {
    private Long leaveId;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String approvalStatus;
    private String remarksNotes;
    private String typeOfLeave;
}
