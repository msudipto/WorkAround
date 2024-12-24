package coms309.controller;

import coms309.dto.SignUpDTO;
import coms309.entity.UserProfile;
import coms309.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/login")
@Tag(name = "User Authentication", description = "Operations related to user login, signup, and password management")
public class LoginController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * Login: Verifies username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return ResponseEntity with login status.
     */
    @Operation(summary = "User login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @GetMapping
    public ResponseEntity<String> login(
            @Parameter(description = "Username of the user", required = true)
            @RequestParam String username,
            @Parameter(description = "Password of the user", required = true)
            @RequestParam String password) {
        Optional<UserProfile> existUser = userProfileRepository.findByUsernameAndPassword(username, password);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed. Invalid credentials.");
        }
        return ResponseEntity.ok("Login successful");
    }

    /**
     * Sign up a new user.
     *
     * @param signUpUser The user signup details.
     * @return ResponseEntity with the result of the signup attempt.
     */
    @Operation(summary = "User signup", responses = {
            @ApiResponse(responseCode = "201", description = "User signed up successfully"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpUser) {
        Optional<UserProfile> existingUser = userProfileRepository.findByUsername(signUpUser.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        // Create and save new user
        UserProfile newUser = new UserProfile();
        newUser.setFullName(signUpUser.getFull_name());
        newUser.setEmail(signUpUser.getEmail());
        newUser.setUsername(signUpUser.getUsername());
        newUser.setPassword(signUpUser.getPassword());
        userProfileRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully.");
    }

    /**
     * Forgot password: Checks if email exists in the database.
     *
     * @param email The email of the user to check.
     * @return ResponseEntity indicating whether the user exists or not.
     */
    @Operation(summary = "Forgot password", responses = {
            @ApiResponse(responseCode = "200", description = "User exists"),
            @ApiResponse(responseCode = "404", description = "No user found with this email")
    })
    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "Email to verify", required = true)
            @RequestParam String email) {
        Optional<UserProfile> existUser = userProfileRepository.findByEmail(email);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user exists with this email.");
        }
        return ResponseEntity.ok("User exists.");
    }

    /**
     * Reset password for a user.
     *
     * @param email The email of the user.
     * @param newPassword The new password to set.
     * @return ResponseEntity indicating the result of the password reset attempt.
     */
    @Operation(summary = "Reset password", responses = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "404", description = "No user found with this email")
    })
    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Email of the user", required = true)
            @RequestParam String email,
            @Parameter(description = "New password for the user", required = true)
            @RequestParam String newPassword) {
        Optional<UserProfile> existUser = userProfileRepository.findByEmail(email);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user exists with this email.");
        }
        UserProfile user = existUser.get();
        user.setPassword(newPassword);
        userProfileRepository.save(user);
        return ResponseEntity.ok("Password successfully updated.");
    }

    /**
     * Paycheck search: Verifies if the user exists by username.
     *
     * @param username The username to search for.
     * @return ResponseEntity indicating whether the user exists or not.
     */
    @Operation(summary = "Paycheck search by username", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/paycheckSearch")
    public ResponseEntity<String> paycheckSearch(
            @Parameter(description = "Username of the user to search", required = true)
            @RequestParam String username) {
        Optional<UserProfile> existUser = userProfileRepository.findByUsername(username);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.ok("User found.");
    }

    /**
     * Retrieves paycheck overview details for a user.
     *
     * @param username The username of the user.
     * @return ResponseEntity with paycheck details.
     */
    @Operation(summary = "Paycheck overview", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paycheck overview"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/paycheckOverview")
    public ResponseEntity<String> paycheckOverview(
            @Parameter(description = "Username of the user to retrieve paycheck details", required = true)
            @RequestParam String username) {
        Optional<UserProfile> existUser = userProfileRepository.findByUsername(username);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        // For demonstration, return static paycheck details
        return ResponseEntity.ok("Take home pay: $2000, Gross pay: $2500, Hours worked: 160, Pay rate: $15, Bonus: $200, Deductibles: $300");
    }

    /**
     * Modify paycheck details for a user (hours worked, pay rate, bonus, and deductibles).
     *
     * @param username The username of the user.
     * @param hoursWorked The number of hours worked.
     * @param payRate The pay rate of the user.
     * @param bonus The bonus amount.
     * @param deductibles The deductibles amount.
     * @return ResponseEntity with the result of the paycheck modification.
     */
    @Operation(summary = "Modify paycheck details", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated paycheck details"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/paycheckModify")
    public ResponseEntity<String> paycheckModify(
            @Parameter(description = "Username of the user to modify paycheck", required = true)
            @RequestParam String username,
            @Parameter(description = "Hours worked by the user", required = true)
            @RequestParam int hoursWorked,
            @Parameter(description = "Pay rate of the user", required = true)
            @RequestParam double payRate,
            @Parameter(description = "Bonus amount", required = true)
            @RequestParam double bonus,
            @Parameter(description = "Deductibles amount", required = true)
            @RequestParam double deductibles) {
        Optional<UserProfile> existUser = userProfileRepository.findByUsername(username);
        if (existUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.ok("Paycheck updated successfully: Hours worked: " + hoursWorked +
                ", Pay rate: " + payRate + ", Bonus: " + bonus + ", Deductibles: " + deductibles);
    }
}
