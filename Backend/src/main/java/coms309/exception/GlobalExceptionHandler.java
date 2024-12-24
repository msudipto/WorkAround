package coms309.exception;

import coms309.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Global exception handler to manage and format error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex ResourceNotFoundException instance.
     * @return ResponseEntity with ApiResponse containing error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles AlreadyClockedInException.
     *
     * @param ex AlreadyClockedInException instance.
     * @return ResponseEntity with ApiResponse containing error details.
     */
    @ExceptionHandler(AlreadyClockedInException.class)
    public ResponseEntity<ApiResponse<String>> handleAlreadyClockedIn(AlreadyClockedInException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic RuntimeExceptions.
     *
     * @param ex RuntimeException instance.
     * @return ResponseEntity with ApiResponse containing error details.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
