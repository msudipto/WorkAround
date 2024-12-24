package coms309.controller;

import coms309.dto.ScheduleDTO;
import coms309.entity.Schedules;
import coms309.exception.ResourceNotFoundException;
import coms309.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller class for handling schedule-related API endpoints.
 */
@RestController
@RequestMapping("/schedules")
@Tag(name = "Schedule Management", description = "Operations for creating, retrieving, updating, and deleting schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * Creates a new schedule.
     *
     * @param scheduleDTO the schedule data transfer object
     * @return the created schedule
     */
    @Operation(summary = "Create a new schedule", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data")
    })
    @PostMapping("/create")
    public ResponseEntity<Schedules> createSchedule(
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        Schedules schedule = scheduleService.createSchedule(scheduleDTO);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    /**
     * Retrieves all schedules.
     *
     * @return a list of all schedules
     */
    @Operation(summary = "Retrieve all schedules", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all schedules")
    })
    @GetMapping
    public ResponseEntity<?> getSchedules() {
        try {
            List<Schedules> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error: Unable to fetch schedules", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a schedule by its ID.
     *
     * @param id the schedule ID
     * @return the schedule with the specified ID
     */
    @Operation(summary = "Retrieve a schedule by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the schedule"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Schedules> getScheduleById(
            @Parameter(description = "ID of the schedule to retrieve", required = true)
            @PathVariable Long id) {
        Schedules schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Retrieves schedules assigned to a specific user.
     *
     * @param userId the user ID
     * @return a list of schedules assigned to the user
     */
    @Operation(summary = "Retrieve schedules by user ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schedules for the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Schedules>> getSchedulesByUser(
            @Parameter(description = "ID of the user to retrieve schedules for", required = true)
            @PathVariable Long userId) {
        List<Schedules> schedules = scheduleService.getSchedulesByUser(userId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Retrieves schedules within a specified date range.
     *
     * @param start the start date of the range
     * @param end   the end date of the range
     * @return a list of schedules within the specified range
     */
    @Operation(summary = "Retrieve schedules within a date range", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schedules within the date range"),
            @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    @GetMapping("/range")
    public ResponseEntity<List<Schedules>> getSchedulesByDateRange(
            @Parameter(description = "Start date of the range", required = true)
            @RequestParam("start") LocalDateTime start,
            @Parameter(description = "End date of the range", required = true)
            @RequestParam("end") LocalDateTime end) {
        List<Schedules> schedules = scheduleService.getSchedulesByDateRange(start, end);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Updates a schedule by its ID.
     *
     * @param id           the schedule ID
     * @param scheduleDTO  the updated schedule data
     * @return the updated schedule
     */
    @Operation(summary = "Update a schedule by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the schedule"),
            @ApiResponse(responseCode = "404", description = "Schedule not found"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Schedules> updateSchedule(
            @Parameter(description = "ID of the schedule to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        Schedules updatedSchedule = scheduleService.updateSchedule(id, scheduleDTO);
        return ResponseEntity.ok(updatedSchedule);
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id the schedule ID
     * @return a response with no content
     */
    @Operation(summary = "Delete a schedule by its ID", responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the schedule"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "ID of the schedule to delete", required = true)
            @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
