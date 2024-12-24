package coms309.repository;

import coms309.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find users by role (e.g., "Admin", "Employer", "Employee")
    List<User> findByRole(String role);

    // Find a user by their name (unique name assumed)
    Optional<User> findByName(String name);

    // Find users assigned to a specific task ID
    List<User> findByTasksSet_Id(Long taskId);

    // Find users with schedules within a certain date range (join with schedules)
    // Additional query methods may be necessary in a custom repository or query annotation
    List<User> findBySchedulesList_StartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find users who have been assigned at least one task
    List<User> findByTasksSetIsNotEmpty();
}