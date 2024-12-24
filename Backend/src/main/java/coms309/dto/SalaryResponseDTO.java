package coms309.dto;

import coms309.entity.Salary;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SalaryResponseDTO {
    private Long salaryId;
    private String username;
    private Double hoursWorked;
    private Double payRate;
    private Double bonusPay;
    private Double deductibles;
    private Double grossPay;
    private Double takeHomePay;
    private Long userProfileId;

    private SalaryResponseDTO mapToSalaryResponseDTO(Salary salary) {
        SalaryResponseDTO dto = new SalaryResponseDTO();
        dto.setSalaryId(salary.getSalaryId());
        dto.setBonusPay(salary.getBonusPay());
        dto.setGrossPay(salary.getGrossPay());
        dto.setDeductibles(salary.getDeductibles());
        dto.setHoursWorked(salary.getHoursWorked());
        dto.setPayRate(salary.getPayRate());
        dto.setTakeHomePay(salary.getTakeHomePay());
        dto.setUserProfileId(salary.getUserProfile().getUserId());  // Assuming Salary has a UserProfile association
        return dto;
    }

}
