
package coms309.controller;

import coms309.dto.ProjectDTO;
import coms309.entity.Projects;
import coms309.repository.ProjectRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import coms309.entity.Projects;
import coms309.service.ProjectService;
import coms309.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/project-status")
@Tag(name = "Project Status", description = "Operations related to project status and tasks")
public class ProjectStatusController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    // Add a new POST mapping to create projects
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    // Get all projects (for admin and employers)
    @GetMapping("/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Projects> projects = projectService.getAllProjects();

        // Ensure mapping logic is handled correctly
        List<ProjectDTO> dtos = projects.stream()
                .map(ProjectDTO::fromEntity) // Use the static `fromEntity` method in ProjectDTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Get projects by username
    @GetMapping("/{username}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUsername(@PathVariable String username) {
        // Fetch projects using the service layer
        List<Projects> projects = projectService.getProjectsByUsername(username);

        // Convert entities to DTOs
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());

        // Return the list of DTOs wrapped in a ResponseEntity
        return ResponseEntity.ok(projectDTOs);
    }

    // Mark a task as completed and update progress
    @PostMapping("/tasks/{id}/complete")
    public ResponseEntity<String> markTaskAsCompleted(@PathVariable Long id) {
        taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok("Task marked as completed successfully.");
    }

    // Get progress of a project
    @GetMapping("/{projectId}/progress")
    public Map<String, Integer> getProjectProgress(@PathVariable Long projectId) {
        int completedTasks = projectService.getCompletedTasks(projectId);
        int totalTasks = projectService.getTotalTasks(projectId);
        Map<String, Integer> progress = new HashMap<>();
        progress.put("completedTasks", completedTasks);
        progress.put("totalTasks", totalTasks);
        return progress;
    }
}