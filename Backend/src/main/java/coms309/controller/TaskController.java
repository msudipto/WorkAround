package coms309.controller;

import coms309.dto.TaskDTO;
import coms309.entity.Tasks;
import coms309.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling task-related API endpoints.
 */
@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Management", description = "Operations for creating, retrieving, updating, and deleting tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    /**
     * Creates a new task.
     *
     * @param taskDTO the task data transfer object
     * @return the created task
     */
    @Operation(summary = "Create a new task", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created the task"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody TaskDTO taskDTO) {
        try {
            Tasks task = taskService.createTask(taskDTO);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating task", e);
            return new ResponseEntity<>("Internal Server Error: Unable to create task", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks
     */
    @Operation(summary = "Retrieve all tasks", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all tasks"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<?> getTasks() {
        try {
            List<Tasks> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving tasks", e);
            return new ResponseEntity<>("Internal Server Error: Unable to fetch tasks", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task ID
     * @return the task with the specified ID
     */
    @Operation(summary = "Retrieve a task by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the task"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true)
            @PathVariable Long id) {
        try {
            Tasks tasks = taskService.getTaskById(id);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving task with ID: " + id, e);
            return new ResponseEntity<>("Internal Server Error: Unable to fetch task", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves tasks by their status.
     *
     * @param status the task status
     * @return a list of tasks with the specified status
     */
    @Operation(summary = "Retrieve tasks by status", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks by status"),
            @ApiResponse(responseCode = "404", description = "Tasks not found for the given status"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(
            @Parameter(description = "Status of the tasks to retrieve", required = true)
            @PathVariable String status) {
        try {
            List<Tasks> tasks = taskService.getTasksByStatus(status);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving tasks with status: " + status, e);
            return new ResponseEntity<>("Internal Server Error: Unable to fetch tasks by status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a task by its ID.
     *
     * @param id        the task ID
     * @param taskDTO   the updated task data
     * @return the updated task
     */
    @Operation(summary = "Update a task by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the task"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TaskDTO taskDTO) {
        try {
            Tasks updatedTasks = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok(updatedTasks);
        } catch (Exception e) {
            logger.error("Error occurred while updating task with ID: " + id, e);
            return new ResponseEntity<>("Internal Server Error: Unable to update task", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the task ID
     * @return a response with no content
     */
    @Operation(summary = "Delete a task by its ID", responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the task"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error occurred while deleting task with ID: " + id, e);
            return new ResponseEntity<>("Internal Server Error: Unable to delete task", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
