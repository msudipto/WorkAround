package coms309.dto;

import java.time.LocalDateTime;

public class TaskUpdateMessageDTO {

    private Long taskId;
    private String taskName;
    private String status;
    private int progress;
    private String message;
    private LocalDateTime timestamp;

    // Constructors
    public TaskUpdateMessageDTO(Long id, String name, String status, int progress) {
        this.timestamp = LocalDateTime.now();
    }

    public TaskUpdateMessageDTO(Long taskId, String taskName, String status, int progress, String message) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        this.progress = progress;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}