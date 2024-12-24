package coms309.repository;

import coms309.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

    // Find schedules by the assigned user's ID
    List<Schedules> findByUserId(Long userId);

    // Find schedules by project ID
    List<Schedules> findByProject_ProjectId(Long projectId);

    // Find schedules within a specific start time range
    List<Schedules> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find schedules by end time within a specific range
    List<Schedules> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find schedules that overlap with a specified time range
    List<Schedules> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(LocalDateTime end, LocalDateTime start);

    // Find schedules by user and date range
    List<Schedules> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // Find schedules by project and date range
    List<Schedules> findByProject_ProjectIdAndStartTimeBetween(Long projectId, LocalDateTime start, LocalDateTime end);

    List<Schedules> findByEmployeeAssignedTo(String employeeAssignedTo);
    List<Schedules> findByEmployerAssignedTo(String employeeAssignedTo);
}