package coms309.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UserDTO {


    private String username;

    @Nullable
    private String email;

    private String password;

    public UserDTO(String username, String email) {
        // Log constructor usage
        System.out.println("Creating UserDTO for username: " + username);

        // Validate username (e.g., ensure it's not null or empty)
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        // Validate email (basic validation for null and regex for correct email format)
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Trim the username to remove extra spaces and standardize the format (e.g., lowercase)
        this.username = username.trim().toLowerCase();

        // Store email after validation
        this.email = email.trim();

        // Log success creation of the object
        System.out.println("UserDTO created successfully for username: " + this.username);
    }
}

