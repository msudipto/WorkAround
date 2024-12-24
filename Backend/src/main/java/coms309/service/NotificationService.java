package coms309.service;

import coms309.dto.NotificationRequestDTO;
import coms309.entity.Notification;
import coms309.entity.Projects;
import coms309.entity.UserProfile;
import coms309.entity.UserType;
import coms309.repository.NotificationRepository;
import coms309.repository.ProjectRepository;
import coms309.repository.UserProfileRepository;
import coms309.websocket.ChatSocket;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ProjectRepository projectsRepository;

    @Autowired
    private ChatSocket chatSocket;

    /**
     * Notify employees about a specific project.
     *
     * @param projectId the ID of the project
     * @return ResponseEntity with a success or error message
     */
    public ResponseEntity<String> notifyEmployees(Long projectId) {
        Optional<Projects> projectOpt = projectsRepository.findById(projectId);
        if (!projectOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with ID: " + projectId);
        }
        Projects project = projectOpt.get();

        // Assuming that employees are users with UserType EMPLOYEE
        List<UserProfile> employees = userProfileRepository.findAllByUserType(UserType.EMPLOYEE);

        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No employees found to notify.");
        }

        for (UserProfile employee : employees) {
            Notification notification = new Notification(
                    "New notification for project: " + project.getProjectName(),
                    "Project Notification",
                    employee,
                    project
            );
            notificationRepository.save(notification);
        }

        return ResponseEntity.ok("Employees notified successfully.");
    }

    /**
     * Create a meeting notification.
     *
     * @param notificationRequestDTO the description of the meeting
     * @return ResponseEntity with a success or error message
     */
    public ResponseEntity<String> createNotification(NotificationRequestDTO notificationRequestDTO) {
        if (notificationRequestDTO.getMessage() == null || notificationRequestDTO.getMessage().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notification message cannot be empty.");
        }
        // Fetch UserProfile
        Optional<UserProfile> userOpt = userProfileRepository.findById(notificationRequestDTO.getUserId());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found with ID: " + notificationRequestDTO.getUserId());
        }
        UserProfile user = userOpt.get();

        // Fetch Project if projectId is provided
        Projects project = null;
        if (notificationRequestDTO.getProjectId() != null) {
            Optional<Projects> projectOpt = projectsRepository.findById(notificationRequestDTO.getProjectId());
            if (!projectOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project not found with ID: " + notificationRequestDTO.getProjectId());
            }
            project = projectOpt.get();
        }
        // Create and save Notification
        Notification notification = new Notification(
                notificationRequestDTO.getMessage(),
                notificationRequestDTO.getType(),
                user,
                project
        );
        notificationRepository.save(notification);

        return ResponseEntity.ok("Notification created successfully.");
    }


    /**
     * Retrieve all notifications.
     *
     * @return ResponseEntity with the list of notifications
     */
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Retrieve a notification by its ID.
     *
     * @param id the ID of the notification
     * @return ResponseEntity with the notification or not found status
     */
    public ResponseEntity<Notification> getNotificationById(Long id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isPresent()) {
            return ResponseEntity.ok(notificationOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Create a new notification.
     *
     * @param notification the notification entity
     * @return ResponseEntity with success message
     */
    public ResponseEntity<String> createNotification(Notification notification) {
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notification message cannot be empty.");
        }

        notification.setTimeStamp(LocalDateTime.now());
        notificationRepository.save(notification);
        return ResponseEntity.ok("Notification created successfully.");
    }

    /**
     * Delete a notification by its ID.
     *
     * @param id the ID of the notification
     * @return ResponseEntity with success or not found message
     */
    public ResponseEntity<String> deleteNotification(Long id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isPresent()) {
            notificationRepository.deleteById(id);
            return ResponseEntity.ok("Notification deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found with ID: " + id);
        }
    }

}

