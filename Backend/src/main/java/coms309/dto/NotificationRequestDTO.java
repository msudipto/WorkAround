package coms309.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NotificationRequestDTO {
    @NotBlank(message = "Notification type cannot be blank")
    private String type;

    @NotBlank(message = "Notification message cannot be blank")
    private String message;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private Long projectId;
}
