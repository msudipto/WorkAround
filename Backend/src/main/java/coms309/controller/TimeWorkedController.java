package coms309.controller;

import coms309.entity.UserProfile;
import coms309.service.UserService;
import coms309.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling time worked related API endpoints.
 */
@RestController
@RequestMapping("/timeWorked")
@Tag(name = "Time Worked Management", description = "Operations for managing user time worked data")
public class TimeWorkedController {

    private static final Logger logger = LoggerFactory.getLogger(TimeWorkedController.class);

    @Autowired
    private UserService userService;

    /**
     * Get user by ID and return timeWorked along with other details.
     *
     * @param id the user ID
     * @return the user profile with time worked
     */
    @Operation(summary = "Get user by ID and return timeWorked", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getTimeWorkedById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        UserProfile user = userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }

    /**
     * Update time worked for a user.
     *
     * @param id the user ID
     * @param userDetails the user profile details to update
     * @return the updated user profile
     */
    @Operation(summary = "Update timeWorked for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user profile"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid user details")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateTimeWorked(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserProfile userDetails) {
        logger.info("Updating timeWorked for user with id: {}", id);
        UserProfile updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Submit user's time for the week.
     *
     * @param user the user profile with time worked for the week
     * @return a response message indicating success or failure
     */
    @Operation(summary = "Submit time for the week", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully submitted time for the week"),
            @ApiResponse(responseCode = "400", description = "Invalid time entry")
    })
    @PostMapping("/timeweek")
    public ResponseEntity<String> submitTimeForWeek(@Valid @RequestBody UserProfile user) {
        logger.info("Submitting time for week for user with id: {}", user.getUserId());
        if (user.getTimeWorked() < 0 || user.getTimeWorked() > 168) {
            return ResponseEntity.badRequest().body("Invalid time entry. Time should be between 0 and 168 hours.");
        }
        // Map DTO to entity and call the service
        boolean result = userService.submitTimeForWeek(user);
        if (!result) {
            return ResponseEntity.badRequest().body("Failed to submit time for the week.");
        }
        return ResponseEntity.ok("Time for the week submitted successfully.");
    }

    /**
     * Unsubmit user's time for the week if they made a mistake.
     *
     * @param user the user profile to unsubmit time for the week
     * @return a response message indicating success or failure
     */
    @Operation(summary = "Unsubmit time for the week", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully unsubmited time for the week"),
            @ApiResponse(responseCode = "400", description = "Failed to unsubmit time for the week")
    })
    @DeleteMapping("/unsubmit")
    public ResponseEntity<String> unsubmitTimeForWeek(@Valid @RequestBody UserProfile user) {
        logger.info("Unsubmitting time for week for user with id: {}", user.getUserId());
        boolean result = userService.unsubmitTimeForWeek(user);
        if (!result) {
            return ResponseEntity.badRequest().body("Failed to unsubmit time for the week.");
        }
        return ResponseEntity.ok("Time for the week unsubmited successfully.");
    }
}
