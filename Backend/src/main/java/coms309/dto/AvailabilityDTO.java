package coms309.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import coms309.entity.Availability;
import coms309.entity.DAY;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Data Transfer Object for Availability entity.
 */
@Getter
@Setter
public class AvailabilityDTO {
    private Long availabilityId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private DAY dayOfWeek;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private AvailabilityDTO convertToDTO(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setAvailabilityId(availability.getAvailabilityId());
        dto.setDayOfWeek(availability.getDayOfWeek());  // This will be mapped correctly to the DTO's `DAY`
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        return dto;
    }

}
