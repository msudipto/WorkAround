package coms309.service;

import coms309.dto.ScheduleDTO;
import coms309.entity.Projects;
import coms309.entity.Schedules;
import coms309.entity.User;
import coms309.exception.ResourceNotFoundException;
import coms309.repository.ProjectRepository;
import coms309.repository.ScheduleRepository;
import coms309.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Create a new schedule
    public Schedules createSchedule(ScheduleDTO scheduleDTO) {
        User user = userRepository.findById(scheduleDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + scheduleDTO.getUserId()));

        Projects project = projectRepository.findByProjectId(scheduleDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + scheduleDTO.getProjectId()));

        Schedules schedule = new Schedules();

        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setEventType(scheduleDTO.getEventType());
        schedule.setUser(user);
        schedule.setProject(project);
        schedule.setUpdatedAt(LocalDateTime.now());
        schedule.setEmployeeAssignedTo(scheduleDTO.getEmployeeAssignedTo());
        schedule.setEmployerAssignedTo(scheduleDTO.getEmployerAssignedTo());

        return scheduleRepository.save(schedule);
    }

    // Retrieve all schedules
    public List<Schedules> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<ScheduleDTO> getSchedulesAssignedTo(String scheduleId) {
        List<Schedules> schedules = scheduleRepository.findByEmployeeAssignedTo(scheduleId);
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    private ScheduleDTO convertToScheduleDTO(Schedules schedules) {
        return new ScheduleDTO(
                schedules.getEventType(),
                schedules.getStartTime(),
                schedules.getEndTime(),
                schedules.getUser().getId(),
                schedules.getProject().getProjectId(),
                schedules.getEmployeeAssignedTo(),
                schedules.getEmployerAssignedTo()
        );
    }


    // Retrieve a schedule by ID
    public Schedules getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedules not found with id: " + scheduleId));
    }

    // Retrieve schedules by user ID
    public List<Schedules> getSchedulesByUser(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }

    // Retrieve schedules within a specific date range
    public List<Schedules> getSchedulesByDateRange(LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByStartTimeBetween(start, end);
    }

    // Update an existing schedule
    @Transactional
    public Schedules updateSchedule(Long scheduleId, ScheduleDTO scheduleDTO) {
        Schedules schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedules not found with id: " + scheduleId));

        User user = userRepository.findById(scheduleDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + scheduleDTO.getUserId()));

        Projects project = projectRepository.findById(scheduleDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + scheduleDTO.getProjectId()));

        schedule.setEventType(scheduleDTO.getEventType());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setUser(user);
        schedule.setProject(project); // Update the project

        return scheduleRepository.save(schedule);
    }

    // Delete a schedule by ID
    public void deleteSchedule(Long scheduleId) {
        Schedules schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedules not found with id: " + scheduleId));

        scheduleRepository.delete(schedule);
    }
}