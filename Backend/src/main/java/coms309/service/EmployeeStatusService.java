package coms309.service;

import coms309.entity.Availability;
import coms309.entity.Employee;
import coms309.entity.LeaveRequests;
import coms309.exception.ResourceNotFoundException;
import coms309.repository.AvailabilityRepository;
import coms309.repository.EmployeeRepository;
import coms309.repository.LeaveRequestsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// Import necessary packages
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Employee Status operations.
 */
@Service
public class EmployeeStatusService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeStatusService.class);

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private LeaveRequestsRepository leaveRequestsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Submit availability for an employee.
     *
     * @param employeeId    ID of the employee.
     * @param availabilities List of Availability entries.
     * @return List of saved Availability entries.
     */
    @Transactional
    public List<Availability> submitAvailability(Long employeeId, List<Availability> availabilities) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Submit availability failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        availabilities.forEach(av -> av.setEmployee(employee));


        List<Availability> savedAvailabilities = availabilityRepository.saveAll(availabilities);

        logger.info("Availability submitted successfully for Employee ID {}", employeeId);
        return savedAvailabilities;
    }


    /**
     * Get unavailable times for an employee.
     *
     * @param employeeId ID of the employee.
     * @return List of Availability entries.
     */
    public List<Availability> getUnavailableTimes(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Get unavailable times failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        List<Availability> availabilities = availabilityRepository.findByEmployee(employee);
        logger.info("Retrieved {} availability entries for Employee ID {}", availabilities.size(), employeeId);
        return availabilities;
    }

    /**
     * Request time off for an employee.
     *
     * @param employeeId  ID of the employee.
     * @param leaveRequest LeaveRequests entry.
     * @return Saved LeaveRequests entry.
     */
    @Transactional
    public LeaveRequests requestTimeOff(Long employeeId, LeaveRequests leaveRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Request time off failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        leaveRequest.setEmployee(employee);
        LeaveRequests savedLeaveRequest = leaveRequestsRepository.save(leaveRequest);

        logger.info("Leave request submitted successfully for Employee ID {}", employeeId);
        return savedLeaveRequest;
    }

    /**
     * Get all time off requests for an employee.
     *
     * @param employeeId ID of the employee.
     * @return List of LeaveRequests entries.
     */
    public List<LeaveRequests> getTimeOffRequests(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Get time off requests failed: Employee with ID {} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });

        List<LeaveRequests> leaveRequests = leaveRequestsRepository.findByEmployee(employee);
        logger.info("Retrieved {} leave requests for Employee ID {}", leaveRequests.size(), employeeId);
        return leaveRequests;
    }

    /**
     * Approve a leave request.
     *
     * @param leaveId ID of the leave request.
     * @return Updated LeaveRequests entry.
     */
    @Transactional
    public LeaveRequests approveLeaveRequest(Long leaveId) {
        LeaveRequests leaveRequest = leaveRequestsRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.error("Approve leave request failed: Leave ID {} not found", leaveId);
                    return new ResourceNotFoundException("Leave request not found");
                });

        leaveRequest.setApprovalStatus("Approved");
        LeaveRequests updatedLeaveRequest = leaveRequestsRepository.save(leaveRequest);

        logger.info("Leave request ID {} approved successfully", leaveId);
        return updatedLeaveRequest;
    }

    /**
     * Reject a leave request.
     *
     * @param leaveId ID of the leave request.
     * @return Updated LeaveRequests entry.
     */
    @Transactional
    public LeaveRequests rejectLeaveRequest(Long leaveId) {
        LeaveRequests leaveRequest = leaveRequestsRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.error("Reject leave request failed: Leave ID {} not found", leaveId);
                    return new ResourceNotFoundException("Leave request not found");
                });

        leaveRequest.setApprovalStatus("Rejected");
        LeaveRequests updatedLeaveRequest = leaveRequestsRepository.save(leaveRequest);

        logger.info("Leave request ID {} rejected successfully", leaveId);
        return updatedLeaveRequest;
    }
}
