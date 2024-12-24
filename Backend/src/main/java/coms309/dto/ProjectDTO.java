
package coms309.dto;

import coms309.entity.Priority;
import coms309.entity.Projects;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Data
public class ProjectDTO {

    private Long projectId;

//    private String description;
//
//    private String projectName;
//
//    private String status;
//
//    private Date dueDate;
//
    private Date startDate;

    private Date endDate;

//    @NotNull(message = "Priority level is required")
//    private Priority priority;
//
//    @NotEmpty(message = "Employers list cannot be empty")
//    private List<@NotBlank(message = "Employer username cannot be blank") String> employerUsernames;

    private String projectName;
    private String description;
    private Date dueDate;
    private Priority priority;
//    private String employerUsername;
    private String status;
    private String employerUsername;
    private int completedTasks;
    private int totalTasks;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static ProjectDTO fromEntity(Projects project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setPriority(Priority.valueOf(project.getPriority()));
        dto.setStatus(project.getStatus());
        dto.setStartDate(java.sql.Date.valueOf(project.getStartDate().toLocalDate()));
        dto.setEndDate(java.sql.Date.valueOf(project.getEndDate().toLocalDate()));
        dto.setDueDate(java.sql.Date.valueOf(project.getDueDate().toLocalDate()));
        return dto;
    }
}
