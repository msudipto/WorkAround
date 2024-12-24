package coms309.service;

import coms309.dto.UserDTO;
import coms309.dto.SignUpDTO;
import coms309.entity.UserProfile;
import coms309.repository.UserProfileRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserProfileRepository userRepository;

    /**
     * Retrieve all users from the database.
     * @return List of users
     */
    public List<UserProfile> getAllUsers() {
        logger.info("Fetching all user profiles");
        return userRepository.findAll();
    }

    /**
     * Get a user by ID.
     * @param id User ID
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<UserProfile> getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Get a user by username.
     * @param username The username of the user
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<UserProfile> getUserByUsername(String username) {
        logger.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Get a user by email.
     * @param email The email of the user
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<UserProfile> getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Create a new user.
     * @param user The user entity to create
     * @return The created user entity
     */
    public UserProfile createUser(UserProfile user) {
        logger.info("Creating new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    /**
     * Update a user by their ID.
     * @param id The ID of the user to update
     * @param userDetails The updated user details
     * @return The updated user entity, or null if not found
     */
    public UserProfile updateUser(Long id, UserProfile userDetails) {
        logger.info("Updating user with ID: {}", id);
        Optional<UserProfile> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            UserProfile existingUser = existingUserOpt.get();
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setUserType(userDetails.getUserType());
            existingUser.setJobTitle(userDetails.getJobTitle());
            existingUser.setDepartment(userDetails.getDepartment());
            existingUser.setDateOfHire(userDetails.getDateOfHire());
            existingUser.setTimeWorked(userDetails.getTimeWorked());
            existingUser.setNextShift(userDetails.getNextShift());
            logger.info("User with ID: {} updated successfully", id);
            return userRepository.save(existingUser);
        }
        logger.warn("User with ID: {} not found for update", id);
        return null;
    }

    /**
     * Update the time worked for a user by their ID.
     * @param id The user ID
     * @param timeWorked The updated time worked value
     * @return The updated user entity, or null if the user is not found
     */
    public UserProfile updateTimeWorked(Long id, int timeWorked) {
        logger.info("Updating time worked for user ID: {}", id);
        Optional<UserProfile> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            UserProfile user = existingUserOpt.get();
            user.setTimeWorked(timeWorked);
            logger.info("Time worked for user ID: {} updated to {}", id, timeWorked);
            return userRepository.save(user);
        }
        logger.warn("User with ID: {} not found for time worked update", id);
        return null;
    }

    /**
     * Delete a user by their ID.
     * @param userId The ID of the user to delete
     * @return True if the user was deleted, false if not found
     */
    public boolean deleteUserByUserId(Long userId) {
        logger.info("Deleting user profile with user_id: {}", userId);
        // Find user by user_id
        Optional<UserProfile> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            logger.info("User profile with user_id: {} deleted successfully", userId);
            return true;
        }
        logger.warn("User profile with user_id: {} not found for deletion", userId);
        return false;
    }

    /**
     * Submit time for a week for a user.
     * @param user The user profile with updated time
     * @return True if successful, false otherwise
     */
    public boolean submitTimeForWeek(@Valid UserProfile user) {
        logger.info("Submitting time for week for user ID: {}", user.getUserId());
        Optional<UserProfile> existingUserOpt = userRepository.findById(user.getUserId());
        if (existingUserOpt.isPresent()) {
            UserProfile existingUser = existingUserOpt.get();
            existingUser.setTimeWorked(user.getTimeWorked());
            userRepository.save(existingUser);
            logger.info("Time submitted successfully for user ID: {}", user.getUserId());
            return true;
        }
        logger.warn("User with ID: {} not found for submitting time", user.getUserId());
        return false;
    }

    /**
     * Unsubmit time for a week for a user.
     * @param user The user profile
     * @return True if successful, false otherwise
     */
    public boolean unsubmitTimeForWeek(@Valid UserProfile user) {
        logger.info("Unsubmitting time for week for user ID: {}", user.getUserId());
        Optional<UserProfile> existingUserOpt = userRepository.findById(user.getUserId());
        if (existingUserOpt.isPresent()) {
            UserProfile existingUser = existingUserOpt.get();
            existingUser.setTimeWorked(0); // Reset timeWorked
            userRepository.save(existingUser);
            logger.info("Time unsubitted successfully for user ID: {}", user.getUserId());
            return true;
        }
        logger.warn("User with ID: {} not found for unsubmitting time", user.getUserId());
        return false;
    }

    /**
     * Retrieve the next shift for a user.
     * @param userId The user ID
     * @return ResponseEntity with the next shift or not found status
     */
    public ResponseEntity<String> getNextShift(Long userId) {
        logger.info("Retrieving next shift for user ID: {}", userId);
        Optional<UserProfile> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Date nextShift = userOpt.get().getNextShift();
            String message = "Next shift for user: " + nextShift;
            logger.info("Next shift for user ID: {} is {}", userId, nextShift);
            return ResponseEntity.ok(message);
        }
        logger.warn("User with ID: {} not found for retrieving next shift", userId);
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieve the time worked for a user.
     * @param userId The user ID
     * @return ResponseEntity with hours worked or not found status
     */
    public ResponseEntity<String> getTimeWorked(Long userId) {
        logger.info("Retrieving time worked for user ID: {}", userId);
        Optional<UserProfile> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            int timeWorked = userOpt.get().getTimeWorked();
            String message = "Hours worked for user: " + timeWorked;
            logger.info("User ID: {} has worked {} hours", userId, timeWorked);
            return ResponseEntity.ok(message);
        }
        logger.warn("User with ID: {} not found for retrieving time worked", userId);
        return ResponseEntity.notFound().build();
    }

    /**
     * Handle password reset for a user.
     * @param forgotUserDTO DTO containing email and new password
     * @return ResponseEntity with a success or failure message
     */
    public ResponseEntity<String> forgotPassword(UserDTO forgotUserDTO) {
        logger.info("Attempting to reset password for email: {}", forgotUserDTO.getEmail());
        Optional<UserProfile> userOpt = userRepository.findByEmail(forgotUserDTO.getEmail());
        if (userOpt.isEmpty()) {
            logger.warn("No user found with email: {}", forgotUserDTO.getEmail());
            return ResponseEntity.badRequest().body("No user exists");
        }
        UserProfile user = userOpt.get();
        user.setPassword(forgotUserDTO.getPassword());
        userRepository.save(user);
        logger.info("Password successfully changed for email: {}", forgotUserDTO.getEmail());
        return ResponseEntity.ok("Successfully changed the password");
    }

    /**
     * Handle user login.
     * @param loginUserDTO DTO containing username and password
     * @return ResponseEntity with a success or failure message
     */
    public ResponseEntity<String> login(UserDTO loginUserDTO) {
        logger.info("Attempting login for username: {}", loginUserDTO.getUsername());
        Optional<UserProfile> userOpt = userRepository.findByUsernameAndPassword(loginUserDTO.getUsername(), loginUserDTO.getPassword());
        if (userOpt.isEmpty()) {
            logger.warn("Login failed for username: {}", loginUserDTO.getUsername());
            return ResponseEntity.badRequest().body("Login failed");
        }
        logger.info("Login successful for username: {}", loginUserDTO.getUsername());
        return ResponseEntity.ok("Login successful");
    }

    /**
     * Handle user signup.
     * @param signUpUserProfileDTO DTO containing signup information
     * @return ResponseEntity with a success or failure message
     */
    public ResponseEntity<String> signup(SignUpDTO signUpUserProfileDTO) {
        logger.info("Attempting signup for username: {}", signUpUserProfileDTO.getUsername());

        // Check if the username already exists
        Optional<UserProfile> existingUsername = userRepository.findByUsername(signUpUserProfileDTO.getUsername());
        if (existingUsername.isPresent()) {
            logger.warn("Signup failed: Username {} already exists", signUpUserProfileDTO.getUsername());
            return ResponseEntity.badRequest().body("Sign up failed: Username already exists");
        }

        // Check if the email already exists
        Optional<UserProfile> existingEmail = userRepository.findByEmail(signUpUserProfileDTO.getEmail());
        if (existingEmail.isPresent()) {
            logger.warn("Signup failed: Email {} already in use", signUpUserProfileDTO.getEmail());
            return ResponseEntity.badRequest().body("Sign up failed: Email already in use");
        }

        // Create and save the new user
        UserProfile newUser = new UserProfile(
                null, // userId (auto-generated by the database)
                signUpUserProfileDTO.getUsername(),
                signUpUserProfileDTO.getEmail(),
                signUpUserProfileDTO.getPassword()
        );
        userRepository.save(newUser);
        logger.info("Signup successful for username: {}", signUpUserProfileDTO.getUsername());
        return ResponseEntity.ok("Sign up successful");
    }
}
