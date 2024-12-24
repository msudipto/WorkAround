
package coms309.service;

import coms309.dto.ProjectDTO;
import coms309.entity.*;
import coms309.repository.EmployerRepository;
import coms309.repository.ProjectRepository;
import coms309.repository.TaskRepository;
import coms309.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class ProjectService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    public int getCompletedTasks(Long projectId) {
        return projectRepository.countCompletedTasks(projectId);
    }

    public int getTotalTasks(Long projectId) {
        return projectRepository.countTotalTasks(projectId);
    }

    public ResponseEntity<Projects> getProjectById(Long projectId) {
        Optional<Projects> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    public ResponseEntity<String> createProject(ProjectDTO projectDTO) {
        Projects project = new Projects();
        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());
        project.setPriority(projectDTO.getPriority().toString());
        project.setStatus(projectDTO.getStatus());
        project.setStartDate(projectDTO.getStartDate().toLocalDate().atStartOfDay());
        project.setEndDate(projectDTO.getEndDate().toLocalDate().atStartOfDay());
        project.setDueDate(projectDTO.getDueDate().toLocalDate().atStartOfDay());

        projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully.");
    }

    public List<Projects> getProjectsByUsername(String username) {
        return projectRepository.findByUsername(username);
    }

    public Projects saveProject(Projects project) {
        return project;
    }
}