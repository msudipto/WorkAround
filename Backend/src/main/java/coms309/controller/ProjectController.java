
package coms309.controller;

import coms309.dto.NotificationRequestDTO;
import coms309.dto.ProjectDTO;
import coms309.entity.Notification;
import coms309.entity.Projects;
import coms309.service.NotificationService;
import coms309.service.UserService;
import coms309.service.ProjectService;
import coms309.service.TaskService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for handling project-related API endpoints.
 */
@RestController
@RequestMapping("/project")
@Tag(name = "Project Management System", description = "API endpoints for project and notification management")
public class ProjectController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final TaskService taskService;

    private Projects convertDtoToEntity(ProjectDTO projectDTO) {
        Projects project = new Projects();
        // Assuming Projects has setName and setDescription methods
        // Add the setName method in the Projects class if it doesn't exist
        project.setUsername(projectDTO.getName());
        // Add the setDescription method in the Projects class if it doesn't exist
        project.setDescription(projectDTO.getDescription());
        return project;
    }

    /**
     * Constructs a new ProjectController with the given services.
     *
     * @param notificationService the notification service
     * @param userService         the user service
     * @param projectService      the project service
     */
    public ProjectController(NotificationService notificationService, UserService userService, ProjectService projectService, TaskService taskService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects
     */
    @Operation(summary = "Retrieve all projects", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all projects")
    })
    @GetMapping("/all")
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id the project ID
     * @return the project with the specified ID
     */
    @Operation(summary = "Retrieve a project by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved project"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/projectId/{id}")
    public ResponseEntity<Projects> getProjectById(
            @Parameter(description = "ID of the project to retrieve", required = true)
            @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    /**
     * Creates a new project.
     *
     * @param projectDTO the project data transfer object
     * @return a response entity with a success message
     */
    @Operation(summary = "Create a new project", responses = {
            @ApiResponse(responseCode = "200", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid project data")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createProject(
            @Valid @RequestBody ProjectDTO projectDTO) {
                Projects project = convertDtoToEntity(projectDTO);
                return projectService.createProject(projectDTO);
    }

    /*
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.updateProject(id, projectDTO);
    }
    */

    /**
     * Sends notifications to employees associated with a project.
     *
     * @param id the project ID
     * @return a response entity with a success message
     */
    @Operation(summary = "Send notifications to employees for a project", responses = {
            @ApiResponse(responseCode = "200", description = "Notifications sent successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping("/notify/{id}")
    public ResponseEntity<String> notifyEmployees(
            @Parameter(description = "ID of the project to notify employees", required = true)
            @PathVariable Long id) {
        return notificationService.notifyEmployees(id);
    }

    /**
     * Creates a new notification.
     *
     * @param notificationRequestDTO the notification request data transfer object
     * @return a response entity with a success message
     */
    @Operation(summary = "Create a new notification", responses = {
            @ApiResponse(responseCode = "200", description = "Notification created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid notification data")
    })
    @PostMapping("/notifications/create")
    public ResponseEntity<String> createNotification(
            @Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
        return notificationService.createNotification(notificationRequestDTO);
    }

    /**
     * Retrieves the next shift for a user.
     *
     * @param id the user ID
     * @return a response entity containing the next shift information
     */
    @Operation(summary = "Retrieve the next shift for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved next shift"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{id}/next-shift")
    public ResponseEntity<String> getNextShift(
            @Parameter(description = "ID of the user to retrieve next shift", required = true)
            @PathVariable Long id) {
        return userService.getNextShift(id);
    }

    /**
     * Retrieves the total time worked by a user.
     *
     * @param id the user ID
     * @return a response entity containing the time worked
     */
    @Operation(summary = "Retrieve total time worked by a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved time worked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{id}/time-worked")
    public ResponseEntity<String> getTimeWorked(
            @Parameter(description = "ID of the user to retrieve time worked", required = true)
            @PathVariable Long id) {
        return userService.getTimeWorked(id);
    }

    /**
     * Retrieves all notifications.
     *
     * @return a response entity containing a list of all notifications
     */
    @Operation(summary = "Retrieve all notifications", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all notifications")
    })
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id the notification ID
     * @return a response entity with a success message
     */
    @Operation(summary = "Delete a notification by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted notification"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @DeleteMapping("/notifications/delete/{id}")
    public ResponseEntity<String> deleteNotification(
            @Parameter(description = "ID of the notification to delete", required = true)
            @PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }

    // Endpoint to get project progress
    @GetMapping("/{projectId}/progress")
    public Map<String, Integer> getProjectProgress(@PathVariable Long projectId) {
        int completedTasks = projectService.getCompletedTasks(projectId);
        int totalTasks = projectService.getTotalTasks(projectId);
        Map<String, Integer> progress = new HashMap<>();
        progress.put("completedTasks", completedTasks);
        progress.put("totalTasks", totalTasks);
        return progress;
    }

    // Endpoint to mark a task as completed
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<String> markTaskAsCompleted(@PathVariable Long taskId) {
        taskService.markTaskAsCompleted(taskId);
        return ResponseEntity.ok("Task marked as completed successfully.");
    }
}
