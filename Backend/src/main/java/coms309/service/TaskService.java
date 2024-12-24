
package coms309.service;

import coms309.dto.TaskDTO;
import coms309.entity.Tasks;
import coms309.entity.Projects;
import coms309.entity.User;
import coms309.exception.ResourceNotFoundException;
import coms309.repository.ProjectRepository;
import coms309.repository.TaskRepository;
import coms309.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public void markTaskAsCompleted(Long taskId) {
        taskRepository.markTaskAsCompleted(taskId);
    }

    // Create a new task
    public Tasks createTask(TaskDTO taskDTO) {
        if (taskDTO.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        // Fetch the project associated with the task
        Projects projects = projectRepository.findByProjectId(taskDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getProjectId()));

        Tasks tasks = new Tasks();

        tasks.setName(taskDTO.getName());
        tasks.setDescription(taskDTO.getDescription());
        tasks.setStatus("Assigned");
        tasks.setProgress(0);
        tasks.setProject(projects); // Set the project
        tasks.setCreatedAt(LocalDateTime.now());
        tasks.setUpdatedAt(LocalDateTime.now());
        tasks.setEmployeeAssignedTo(taskDTO.getEmployeeAssignedTo());
        tasks.setEmployerAssignedTo(taskDTO.getEmployerAssignedTo());

        taskRepository.save(tasks);

        // Notify all users about the new tasks creation
//        taskWebSocketService.sendTaskNotification("New tasks created: " + tasks.getName());

        return tasks;
    }

    // Retrieve all tasks
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    // Retrieve task by ID
    public Tasks getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with id: " + taskId));
    }

    public List<Tasks> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<TaskDTO> getTasksAssignedTo(String taskId) {
        List<Tasks> tasks = taskRepository.findByEmployeeAssignedTo(taskId);
        // Convert entity list to DTO list (using a mapping method or library)
        return tasks.stream().map(this::convertToTaskDTO).collect(Collectors.toList());
    }

    private TaskDTO convertToTaskDTO(Tasks task) {
        return new TaskDTO(
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getProgress(),
                task.getProject(),
                task.getEmployeeAssignedTo(),
                task.getEmployerAssignedTo()
        );
    }

    public List<Tasks> getTasksByEmployer(String employerId) {
        return taskRepository.findByEmployerAssignedTo(employerId);
    }

    // Update an existing task
    @Transactional
    public Tasks updateTask(Long taskId, TaskDTO taskDTO) {
        Tasks tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with id: " + taskId));

        tasks.setStatus(taskDTO.getStatus());
        tasks.setProgress(taskDTO.getProgress());
        taskRepository.save(tasks);

        // Trigger WebSocket update to all relevant users
//        taskWebSocketService.sendTaskUpdate(tasks);

        return tasks;
    }

    // Delete a task by ID
    public void deleteTask(Long taskId) {
        Tasks tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with id: " + taskId));

        taskRepository.delete(tasks);

        // Notify about tasks deletion
//        taskWebSocketService.sendTaskNotification("Tasks deleted: " + tasks.getName());
    }

    // Assign a user to a task
    @Transactional
    public Tasks assignTaskToUser(Long taskId, Long userId) {
        Tasks tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        tasks.getAssignedEmployees().add(user);
        taskRepository.save(tasks);

        // Notify the specific user about tasks assignment
//        taskWebSocketService.sendTaskUpdateToUser(userId, tasks);

        return tasks;
    }
}
