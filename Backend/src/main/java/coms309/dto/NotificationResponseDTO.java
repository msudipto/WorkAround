package coms309.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class NotificationResponseDTO {
    private Long notificationId;
    private String type;
    private String message;
    private LocalDateTime timeStamp;
    private String username;
    private String projectName;
}
