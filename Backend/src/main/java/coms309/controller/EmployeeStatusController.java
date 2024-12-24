package coms309.controller;

import coms309.dto.ApiResponse;
import coms309.dto.AvailabilityDTO;
import coms309.dto.LeaveRequestDTO;
import coms309.entity.Availability;
import coms309.entity.DAY;
import coms309.entity.LeaveRequests;
import coms309.service.EmployeeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing Employee Status operations, including availability and leave requests.
 */
@RestController
@RequestMapping("/api/status")
@Tag(name = "Employee Status", description = "Operations related to employee status, availability, and leave requests")
public class EmployeeStatusController {

    @Autowired
    private EmployeeStatusService statusService;

    /**
     * POST: Submit Availability for an employee.
     *
     * @param employeeId       ID of the employee.
     * @param requestBody List of AvailabilityDTO entries.
     * @return ResponseEntity containing ApiResponse with list of saved AvailabilityDTOs.
     */
    @PostMapping("/availability")
    public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> submitAvailability(
            @RequestParam Long employeeId,
            @RequestBody Map<String, List<AvailabilityDTO>> requestBody) {

        // Retrieve the list of availabilityDTOs from the request body
        List<AvailabilityDTO> availabilityDTOs = requestBody.get("availabilityDTOs");

        // Convert the DTOs into Availability entities
        List<Availability> availabilities = availabilityDTOs.stream()
                .map(dto -> {
                    Availability av = new Availability();
                    av.setDayOfWeek(DAY.valueOf(dto.getDayOfWeek().toString()));  // Convert DTO's DayOfWeek to the Enum
                    av.setStartTime(dto.getStartTime());
                    av.setEndTime(dto.getEndTime());
                    return av;
                })
                .collect(Collectors.toList());

        // Submit the availability data to the service layer
        List<Availability> savedAvailabilities = statusService.submitAvailability(employeeId, availabilities);

        // Convert saved Availability entities back to DTOs for response
        List<AvailabilityDTO> savedDTOs = savedAvailabilities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Send the response with the saved availability
        ApiResponse<List<AvailabilityDTO>> response = new ApiResponse<>(
                true,
                "Availability submitted successfully",
                savedDTOs
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * GET: Retrieve Unavailable Times for an employee.
     *
     * @param employeeId ID of the employee.
     * @return ResponseEntity containing ApiResponse with list of AvailabilityDTOs.
     */
    @GetMapping("/unavailable")
    public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUnavailableTimes(@RequestParam Long employeeId) {
        List<Availability> availabilities = statusService.getUnavailableTimes(employeeId);
        List<AvailabilityDTO> availabilityDTOs = availabilities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ApiResponse<List<AvailabilityDTO>> response = new ApiResponse<>(
                true,
                "Unavailable times retrieved successfully",
                availabilityDTOs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST: Submit Leave Request for an employee.
     *
     * @param employeeId      ID of the employee.
     * @param leaveRequestDTO LeaveRequestDTO entry.
     * @return ResponseEntity containing ApiResponse with saved LeaveRequestDTO.
     */
    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> submitLeaveRequest(
            @RequestParam Long employeeId,
            @RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequests leaveRequest = new LeaveRequests();
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setDescription(leaveRequestDTO.getDescription());
        leaveRequest.setTypeOfLeave(leaveRequestDTO.getTypeOfLeave());
        leaveRequest.setRemarksNotes(leaveRequestDTO.getRemarksNotes());
        // approvalStatus and requestDate are handled in the service layer or entity defaults

        LeaveRequests savedLeaveRequest = statusService.requestTimeOff(employeeId, leaveRequest);
        LeaveRequestDTO savedDTO = convertToDTO(savedLeaveRequest);

        ApiResponse<LeaveRequestDTO> response = new ApiResponse<>(
                true,
                "Leave request submitted successfully",
                savedDTO
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET: Retrieve All Leave Requests for an employee.
     *
     * @param employeeId ID of the employee.
     * @return ResponseEntity containing ApiResponse with list of LeaveRequestDTOs.
     */
    @GetMapping("/leave")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getLeaveRequests(@RequestParam Long employeeId) {
        List<LeaveRequests> leaveRequests = statusService.getTimeOffRequests(employeeId);
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ApiResponse<List<LeaveRequestDTO>> response = new ApiResponse<>(
                true,
                "Leave requests retrieved successfully",
                leaveRequestDTOs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST: Approve a Leave Request.
     *
     * @param leaveId ID of the leave request.
     * @return ResponseEntity containing ApiResponse with updated LeaveRequestDTO.
     */
    @PostMapping("/leave/approve")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> approveLeaveRequest(@RequestParam Long leaveId) {
        LeaveRequests updatedLeave = statusService.approveLeaveRequest(leaveId);
        LeaveRequestDTO updatedDTO = convertToDTO(updatedLeave);

        ApiResponse<LeaveRequestDTO> response = new ApiResponse<>(
                true,
                "Leave request approved successfully",
                updatedDTO
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST: Reject a Leave Request.
     *
     * @param leaveId ID of the leave request.
     * @return ResponseEntity containing ApiResponse with updated LeaveRequestDTO.
     */
    @PostMapping("/leave/reject")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> rejectLeaveRequest(@RequestParam Long leaveId) {
        LeaveRequests updatedLeave = statusService.rejectLeaveRequest(leaveId);
        LeaveRequestDTO updatedDTO = convertToDTO(updatedLeave);

        ApiResponse<LeaveRequestDTO> response = new ApiResponse<>(
                true,
                "Leave request rejected successfully",
                updatedDTO
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Utility method to convert Availability entity to AvailabilityDTO.
     *
     * @param availability Availability entity.
     * @return AvailabilityDTO.
     */
    private AvailabilityDTO convertToDTO(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setAvailabilityId(availability.getAvailabilityId());
        dto.setDayOfWeek(availability.getDayOfWeek());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        return dto;
    }

    /**
     * Utility method to convert LeaveRequests entity to LeaveRequestDTO.
     *
     * @param leave LeaveRequests entity.
     * @return LeaveRequestDTO.
     */
    private LeaveRequestDTO convertToDTO(LeaveRequests leave) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setLeaveId(leave.getLeaveId());
        dto.setEmployeeId(leave.getEmployee().getEmployeeId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setDescription(leave.getDescription());
        dto.setApprovalStatus(leave.getApprovalStatus());
        dto.setRemarksNotes(leave.getRemarksNotes());
        dto.setTypeOfLeave(leave.getTypeOfLeave());
        return dto;
    }
}
