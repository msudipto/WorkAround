package coms309.dto;

import coms309.entity.Projects;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class TaskDTO {

    @NotBlank(message = "Task name is required")
    @Size(max = 100, message = "Task name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Status is required")
    private String status;  // e.g., "Assigned", "In Progress", "Completed"

    @Min(value = 0, message = "Progress must be at least 0%")
    @Max(value = 100, message = "Progress must not exceed 100%")
    private Integer progress;  // Represented as percentage (0-100)

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Project ID is required")
    private String employeeAssignedTo;

    @NotNull(message = "Project ID is required")
    private String employerAssignedTo;

    // Constructors
    public TaskDTO() {}

    public TaskDTO(String name, String description, String status, Integer progress, Projects projectId, String EmployeeAssignedTo, String EmployerAssignedTo) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.progress = progress;
        this.projectId = projectId.getProjectId();
        this.employeeAssignedTo = EmployeeAssignedTo;
        this.employerAssignedTo = EmployerAssignedTo;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getEmployeeAssignedTo() {
        return employeeAssignedTo;
    }

    public void setEmployeeAssignedTo(String employeeAssignedTo) {
        this.employeeAssignedTo = employeeAssignedTo;
    }

    public String getEmployerAssignedTo() {
        return employerAssignedTo;
    }

    public void setEmployerAssignedTo(String employerAssignedTo) {
        this.employerAssignedTo = employerAssignedTo;
    }
}