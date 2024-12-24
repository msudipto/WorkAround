package coms309.dto;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;

/**
 * Data Transfer Object for TimeLog entity.
 */
@Getter
@Setter
public class TimeLogDTO {
    private Long timeLogId;
    private Long employeeId;
    private Date logDate;
    private Date clockInTime;
    private Date clockOutTime;
}
