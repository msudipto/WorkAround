package coms309.controller;

import coms309.dto.SignUpDTO;
import coms309.dto.UserDTO;
import coms309.entity.UserProfile;
import coms309.service.UserService;
import coms309.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling user profile-related API endpoints.
 */
@RestController
@RequestMapping("/api/userprofile")
@Tag(name = "User Profile Management", description = "Operations for creating, retrieving, updating, and managing user profiles")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserService userService;

    /**
     * Retrieve all user profiles.
     *
     * @return List of all users.
     */
    @Operation(summary = "Retrieve all user profiles", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all user profiles")
    })
    @GetMapping("/all")
    public ResponseEntity<List<UserProfile>> getAllUserProfiles() {
        logger.info("Controller: Fetching all user profiles");

        try {
            List<UserProfile> users = userService.getAllUsers();

            if (users.isEmpty()) {
                logger.info("No user profiles found.");
                return ResponseEntity.noContent().build();
            }

            // Map to DTOs to prevent serialization issues
            List<UserProfile> userDTOs = users.stream()
                    .map(user -> new UserProfile(
                            user.getUserId(),
                            user.getFullName(),
                            user.getUsername(),
                            user.getEmail()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);

        } catch (Exception e) {
            logger.error("Error occurred while fetching user profiles: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * Retrieve a user profile by ID.
     *
     * @param id The user ID.
     * @return The user profile if found.
     */
    @Operation(summary = "Retrieve a user profile by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        logger.info("Controller: Fetching user profile with ID: {}", id);
        Optional<UserProfile> userProfileOpt = userService.getUserById(id);
        return userProfileOpt.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Controller: User profile not found for ID: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    /**
     * Retrieve a user profile by username.
     *
     * @param username The username of the user.
     * @return The user profile if found.
     */
    @Operation(summary = "Retrieve a user profile by username", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfile> getUserProfileByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true)
            @PathVariable String username) {
        logger.info("Controller: Fetching user profile with username: {}", username);
        Optional<UserProfile> userProfileOpt = userService.getUserByUsername(username);
        return userProfileOpt.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Controller: User profile not found for username: {}", username);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    /**
     * Retrieve a user profile by email.
     *
     * @param email The email of the user.
     * @return The user profile if found.
     */
    @Operation(summary = "Retrieve a user profile by email", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfile> getUserProfileByEmail(
            @Parameter(description = "Email of the user to retrieve", required = true)
            @PathVariable String email) {
        logger.info("Controller: Fetching user profile with email: {}", email);
        Optional<UserProfile> userProfileOpt = userService.getUserByEmail(email);
        return userProfileOpt.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Controller: User profile not found for email: {}", email);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    /**
     * Create a new user profile.
     *
     * @param user The user profile to create.
     * @return The created user profile.
     */
    @Operation(summary = "Create a new user profile", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created the user profile"),
            @ApiResponse(responseCode = "400", description = "Invalid user profile data")
    })
    @PostMapping("/create")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile user) {
        logger.info("Controller: Creating new user profile");
        UserProfile createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Update an existing user profile by ID.
     *
     * @param id   The ID of the user to update.
     * @param user The updated user profile details.
     * @return The updated user profile if successful.
     */
    @Operation(summary = "Update an existing user profile by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user profile"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @RequestBody UserProfile user) {
        logger.info("Controller: Updating user profile with ID: {}", id);
        UserProfile updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.warn("Controller: User profile not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Delete a user profile by ID.
     *
     * @param userId The ID of the user to delete.
     * @return ResponseEntity indicating the outcome.
     */
    @Operation(summary = "Delete a user profile by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the user profile"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserProfileByUserId(@PathVariable Long userId) {
        logger.info("Controller: Deleting user profile with user_id: {}", userId);
        boolean deleted = userService.deleteUserByUserId(userId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204
        } else {
            logger.warn("Controller: User profile not found for user_id: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // HTTP 404
        }
    }

    /**
     * Submit time for a week for a user.
     *
     * @param user The user profile with updated time.
     * @return ResponseEntity indicating the outcome.
     */
    @Operation(summary = "Submit time for a week for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully submitted time for the week"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @PostMapping("/submitTime")
    public ResponseEntity<String> submitTimeForWeek(@Valid @RequestBody UserProfile user) {
        logger.info("Controller: Submitting time for week for user ID: {}", user.getUserId());
        boolean success = userService.submitTimeForWeek(user);
        if (success) {
            return ResponseEntity.ok("Time submitted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    /**
     * Unsubmit time for a week for a user.
     *
     * @param user The user profile.
     * @return ResponseEntity indicating the outcome.
     */
    @Operation(summary = "Unsubmit time for a week for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully unsubmited time for the week"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @PostMapping("/unsubmitTime")
    public ResponseEntity<String> unsubmitTimeForWeek(@Valid @RequestBody UserProfile user) {
        logger.info("Controller: Unsubmitting time for week for user ID: {}", user.getUserId());
        boolean success = userService.unsubmitTimeForWeek(user);
        if (success) {
            return ResponseEntity.ok("Time unsubmited successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    /**
     * Retrieve the next shift for a user.
     *
     * @param userId The ID of the user.
     * @return The next shift details if found.
     */
    @Operation(summary = "Retrieve the next shift for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved next shift"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/{userId}/nextShift")
    public ResponseEntity<String> getNextShift(@PathVariable Long userId) {
        logger.info("Controller: Retrieving next shift for user ID: {}", userId);
        return userService.getNextShift(userId);
    }

    /**
     * Retrieve the time worked for a user.
     *
     * @param userId The ID of the user.
     * @return The time worked details if found.
     */
    @Operation(summary = "Retrieve time worked for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved time worked"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/{userId}/timeWorked")
    public ResponseEntity<String> getTimeWorked(@PathVariable Long userId) {
        logger.info("Controller: Retrieving time worked for user ID: {}", userId);
        return userService.getTimeWorked(userId);
    }

    /**
     * Handle password reset for a user.
     *
     * @param forgotUserDTO DTO containing email and new password.
     * @return ResponseEntity indicating the outcome.
     */

    /**
     * Gets all usernames.
     */
    @Operation(summary = "Retrieve all usernames", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all usernames")
    })
    @GetMapping("/usernames")
    public ResponseEntity<List<UserProfile>> getAllUsernames() {
        logger.info("Controller: Fetching all usernames");
        List<UserProfile> usernames = userService.getAllUsers();
        return ResponseEntity.ok(usernames);
    }
}
