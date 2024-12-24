package coms309.controller;

import coms309.dto.ApiResponse;
import coms309.dto.TimeLogDTO;
import coms309.entity.TimeLog;
import coms309.service.EmployeeAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing Employee Attendance operations.
 */
@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Employee Attendance", description = "Operations related to employee attendance tracking")
public class EmployeeAttendanceController {

    @Autowired
    private EmployeeAttendanceService attendanceService;

    /**
     * POST: Clock In an employee.
     *
     * @param employeeId ID of the employee.
     * @return ResponseEntity containing ApiResponse with TimeLogDTO.
     */
    @PostMapping("/clock-in")
    public ResponseEntity<ApiResponse<TimeLogDTO>> clockIn(@RequestParam Long employeeId) {
        TimeLog timeLog = attendanceService.clockIn(employeeId);
        TimeLogDTO timeLogDTO = convertToDTO(timeLog);
        ApiResponse<TimeLogDTO> response = new ApiResponse<>(
                true,
                "Clock-in successful",
                timeLogDTO
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * POST: Clock Out an employee.
     *
     * @param employeeId ID of the employee.
     * @return ResponseEntity containing ApiResponse with TimeLogDTO.
     */
    @PostMapping("/clock-out")
    public ResponseEntity<ApiResponse<TimeLogDTO>> clockOut(@RequestParam Long employeeId) {
        TimeLog timeLog = attendanceService.clockOut(employeeId);
        TimeLogDTO timeLogDTO = convertToDTO(timeLog);
        ApiResponse<TimeLogDTO> response = new ApiResponse<>(
                true,
                "Clock-out successful",
                timeLogDTO
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * GET: Retrieve all employees currently clocked in.
     *
     * @return ResponseEntity containing ApiResponse with list of TimeLogDTOs.
     */
    @GetMapping("/currently-working")
    public ResponseEntity<ApiResponse<List<TimeLogDTO>>> getCurrentlyWorking() {
        List<TimeLog> activeLogs = attendanceService.getCurrentlyWorking();
        List<TimeLogDTO> timeLogDTOs = activeLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        ApiResponse<List<TimeLogDTO>> response = new ApiResponse<>(
                true,
                "Currently working employees retrieved successfully",
                timeLogDTOs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET: Retrieve daily attendance summary.
     *
     * @return ResponseEntity containing ApiResponse with list of TimeLogDTOs.
     */
    @GetMapping("/daily-summary")
    public ResponseEntity<ApiResponse<List<TimeLogDTO>>> getDailySummary() {
        List<TimeLog> dailyLogs = attendanceService.getDailyAttendance();
        List<TimeLogDTO> timeLogDTOs = dailyLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        ApiResponse<List<TimeLogDTO>> response = new ApiResponse<>(
                true,
                "Daily attendance summary retrieved successfully",
                timeLogDTOs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Utility method to convert TimeLog entity to TimeLogDTO.
     *
     * @param timeLog TimeLog entity.
     * @return TimeLogDTO.
     */
    private TimeLogDTO convertToDTO(TimeLog timeLog) {
        TimeLogDTO dto = new TimeLogDTO();
        dto.setTimeLogId(timeLog.getTimeLogId());
        dto.setEmployeeId(timeLog.getEmployee().getEmployeeId());
        dto.setLogDate(timeLog.getLogDate());
        dto.setClockInTime(timeLog.getClockInTime());
        if (timeLog.getClockOutTime() != null) {
            dto.setClockOutTime(timeLog.getClockOutTime());
        }
        return dto;
    }

}
