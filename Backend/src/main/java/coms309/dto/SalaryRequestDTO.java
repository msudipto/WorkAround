package coms309.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SalaryRequestDTO {
    @NotNull(message = "Salary Id cannot be null ")
    private Long salaryId;


    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Hours worked cannot be null")
    @Positive(message = "Hours worked must be positive")
    private Double hoursWorked;

    @NotNull(message = "Pay rate cannot be null")
    @Positive(message = "Pay rate must be positive")
    private Double payRate;


    private Double bonusPay = 0.0;
    private Double deductibles = 0.0;

    private Double grossPay;
    private Double takeHomePay;

}
