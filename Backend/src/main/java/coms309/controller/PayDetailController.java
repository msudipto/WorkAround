package coms309.controller;

import coms309.dto.SalaryResponseDTO;
import coms309.entity.Salary;
import coms309.service.PayDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing salary details.
 */
@RestController
@RequestMapping("/api/salary")
@Tag(name = "Salary Management System", description = "Operations related to salary management")
public class PayDetailController {

    @Autowired
    private PayDetailService payDetailService;

    /**
     * Get salary details for a user.
     *
     * @param userId the user ID
     * @return the salary details
     */
    @Operation(summary = "Get salary details for a user by user ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved salary details"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSalaryForUser(
            @Parameter(description = "ID of the user to retrieve salary", required = true)
            @PathVariable Long userId) {
        return payDetailService.getSalaryForUserResponse(userId);
    }

    /**
     * Get all salaries for a user.
     *
     * @param userId the user ID
     * @return all salaries
     */
    @Operation(summary = "Get all salary records for a user by user ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all salary records"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllSalariesForUser(
            @PathVariable Long userId) {
        return payDetailService.getAllSalariesForUserResponse(userId);
    }



    /**
     * Create or update salary details.
     *
     * @param salary the salary details
     * @return the updated or created salary
     */
    @Operation(summary = "Create or update salary details", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created or updated salary"),
            @ApiResponse(responseCode = "400", description = "Invalid salary details")
    })
    @PostMapping("/update")
    public ResponseEntity<?> createOrUpdateSalary(
            @Parameter(description = "Salary object to create or update", required = true)
            @RequestBody Salary salary) {
        return payDetailService.createOrUpdateSalaryResponse(salary);
    }

    /**
     * Get salary details for a user by username.
     *
     * @param username the username
     * @return the salary details
     */
    @Operation(summary = "Get salary details for a user by username", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved salary details"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getSalaryByUsername(
            @Parameter(description = "Username of the user to retrieve salary", required = true)
            @PathVariable String username) {
        return payDetailService.getSalaryByUsernameResponse(username);
    }

    /**
     * Delete salary details by ID.
     *
     * @param salaryId the salary ID
     * @return the response indicating success or failure
     */
    @Operation(summary = "Delete salary details by salary ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted salary"),
            @ApiResponse(responseCode = "404", description = "Salary not found")
    })
    @DeleteMapping("/delete/{salaryId}")
    public ResponseEntity<?> deleteSalary(
            @Parameter(description = "ID of the salary to delete", required = true)
            @PathVariable Long salaryId) {
        return payDetailService.deleteSalaryResponse(salaryId);
    }
}
