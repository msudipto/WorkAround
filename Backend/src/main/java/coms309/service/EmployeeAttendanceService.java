package coms309.service;
import coms309.entity.Employee;
import coms309.entity.TimeLog;
import coms309.exception.AlreadyClockedInException;
import coms309.exception.ResourceNotFoundException;
import coms309.repository.EmployeeRepository;
import coms309.repository.TimeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeAttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAttendanceService.class);

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Clock in an employee.
     *
     * @param employeeId ID of the employee
     * @return The created TimeLog entry
     */
    @Transactional
    public TimeLog clockIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Clock-in failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        LocalDate today = LocalDate.now();
        List<TimeLog> todaysLogs = timeLogRepository.findByEmployeeAndLogDate(employee, Date.valueOf(today));
        boolean alreadyClockedIn = todaysLogs.stream()
                .anyMatch(log -> log.getClockOutTime() == null);

        if (alreadyClockedIn) {
            logger.warn("Clock-in attempt failed: Employee with ID {} is already clocked in", employeeId);
            throw new AlreadyClockedInException("Employee already clocked in");
        }

        TimeLog timeLog = new TimeLog();
        timeLog.setEmployee(employee);
        timeLog.setLogDate(Date.valueOf(today));
        timeLog.setClockInTime(timeLog.getClockInTime());
        timeLogRepository.save(timeLog);

        logger.info("Employee with ID {} clocked in successfully at {}", employeeId, timeLog.getClockInTime());

        return timeLog;
    }
    /**
     * Clock out an employee.
     *
     * @param employeeId ID of the employee.
     * @return The updated TimeLog entry.
     */
    @Transactional
    public TimeLog clockOut(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Clock-out failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        LocalDate today = LocalDate.now();
        Date sqlToday = java.sql.Date.valueOf(today);
        List<TimeLog> todaysLogs = timeLogRepository.findByEmployeeAndLogDate(employee, sqlToday);

        TimeLog activeLog = todaysLogs.stream()
                .filter(log -> log.getClockOutTime() == null)
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Clock-out attempt failed: No active clock-in found for Employee ID {}", employeeId);
                    return new RuntimeException("Employee is not clocked in");
                });

        activeLog.setClockOutTime(new java.util.Date());
        timeLogRepository.save(activeLog);

        logger.info("Employee with ID {} clocked out successfully at {}", employeeId, activeLog.getClockOutTime());

        return activeLog;
    }

    /**
     * Get all employees currently clocked in.
     *
     * @return List of active TimeLog entries.
     */
    public List<TimeLog> getCurrentlyWorking() {
        LocalDate today = LocalDate.now();
        Date sqlToday = java.sql.Date.valueOf(today);
        List<TimeLog> activeLogs = timeLogRepository.findByLogDateAndClockOutTimeIsNull(sqlToday);
        logger.info("Retrieved {} employees currently working", activeLogs.size());
        return activeLogs;
    }

    /**
     * Get daily attendance summary.
     *
     * @return List of TimeLog entries for the current day.
     */
    public List<TimeLog> getDailyAttendance() {
        LocalDate today = LocalDate.now();
        Date sqlToday = java.sql.Date.valueOf(today);
        List<TimeLog> dailyLogs = timeLogRepository.findByLogDate(sqlToday);
        logger.info("Retrieved {} TimeLog entries for daily attendance", dailyLogs.size());
        return dailyLogs;
    }
}
