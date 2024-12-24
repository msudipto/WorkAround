package coms309.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic API Response Wrapper.
 *
 * @param <T> Type of the response data.
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
