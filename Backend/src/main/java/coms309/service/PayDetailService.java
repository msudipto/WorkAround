package coms309.service;

import coms309.dto.SalaryRequestDTO;
import coms309.dto.SalaryResponseDTO;

import coms309.entity.Salary;
import coms309.entity.UserProfile;
import coms309.exception.ResourceNotFoundException;
import coms309.repository.SalaryRepository;
import coms309.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayDetailService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * Get salary details for a user by user ID.
     *
     * @param userId the user ID
     * @return the salary details wrapped in ResponseEntity
     */
    public ResponseEntity<?> getSalaryForUserResponse(Long userId) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findById(userId);
        if (!userProfileOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
        UserProfile userProfile = userProfileOpt.get();
        Optional<Salary> salary = salaryRepository.findByUserProfile(userProfile);
        if (salary.isPresent()) {
            SalaryResponseDTO responseDTO = mapToSalaryResponseDTO(salary.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salary details not found for user.");
        }

    }

    /**
     * Get all salaries for a user by user ID.
     *
     * @param userId the user ID
     * @return a list of all salaries wrapped in ResponseEntity
     */
    public ResponseEntity<?> getAllSalariesForUserResponse(Long userId) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findById(userId);
        if (!userProfileOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }

        UserProfile userProfile = userProfileOpt.get();
        List<Salary> salaries = salaryRepository.findAllByUserProfile(userProfile);

        if (!salaries.isEmpty()) {
            List<SalaryResponseDTO> responseDTOs = salaries.stream()
                    .map(this::mapToSalaryResponseDTO)  // Map each Salary to SalaryResponseDTO
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No salaries found for user.");
        }
    }


    /**
     * Get salary details for a user by username.
     *
     * @param username the username
     * @return the salary details wrapped in ResponseEntity
     */
    public ResponseEntity<?> getSalaryByUsernameResponse(String username) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUsername(username);
        if (!userProfileOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with username: " + username);
        }
        UserProfile userProfile = userProfileOpt.get();
        Optional<Salary> salaryOpt = salaryRepository.findByUserProfile(userProfile);
        if (salaryOpt.isPresent()) {
            SalaryResponseDTO responseDTO = mapToSalaryResponseDTO(salaryOpt.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salary details not found for user.");
        }
    }

    /**
     * Create or update salary details for a user.
     *
     * @param salary the salary details
     * @return the created or updated salary entity wrapped in ResponseEntity
     */
    public ResponseEntity<?> createOrUpdateSalaryResponse(Salary salary ) {
        Long userId = salary.getUserProfile().getUserId(); // Assuming getUserId() exists
        Optional<UserProfile> userProfileOpt = userProfileRepository.findById(userId);
        if (!userProfileOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found with ID: " + userId);
        }
        UserProfile userProfile = userProfileOpt.get();
        salary.setUserProfile(userProfile);

        Optional<Salary> existingSalaryOpt = salaryRepository.findByUserProfile(userProfile);
        Salary savedSalary;

        if (existingSalaryOpt.isPresent()) {
            Salary existingSalary = existingSalaryOpt.get();
            existingSalary.setHoursWorked(salary.getHoursWorked());
            existingSalary.setPayRate(salary.getPayRate());
            existingSalary.setBonusPay(salary.getBonusPay() != null ? salary.getBonusPay() : 0.0);
            existingSalary.setDeductibles(salary.getDeductibles() != null ? salary.getDeductibles() : 0.0);
            existingSalary.setGrossPay(existingSalary.calculateGrossPay());
            existingSalary.setTakeHomePay(existingSalary.calculateTakeHomePay());
            savedSalary = salaryRepository.save(existingSalary);
        } else {
            if (salary.getBonusPay() == null) {
                salary.setBonusPay(0.0);
            }
            if (salary.getDeductibles() == null) {
                salary.setDeductibles(0.0);
            }
            salary.setGrossPay(salary.calculateGrossPay());
            salary.setTakeHomePay(salary.calculateTakeHomePay());
            savedSalary = salaryRepository.save(salary);
        }
        SalaryResponseDTO responseDTO = mapToSalaryResponseDTO(savedSalary);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    /**
     * Delete a salary by ID.
     *
     * @param salaryId the ID of the salary to delete
     * @return a ResponseEntity indicating the outcome of the operation
     */
    public ResponseEntity<?> deleteSalaryResponse(Long salaryId) {
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        if (salary.isPresent()) {
            salaryRepository.deleteById(salaryId);
            return ResponseEntity.ok("Salary deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salary not found with ID: " + salaryId);
        }
    }
    private SalaryResponseDTO mapToSalaryResponseDTO (Salary salary){
        SalaryResponseDTO dto = new SalaryResponseDTO();
        dto.setSalaryId(salary.getSalaryId());
        dto.setUsername(salary.getUserProfile().getUsername());
        dto.setHoursWorked(salary.getHoursWorked());
        dto.setPayRate(salary.getPayRate());
        dto.setBonusPay(salary.getBonusPay());
        dto.setGrossPay(salary.getGrossPay());
        dto.setTakeHomePay(salary.getTakeHomePay());
        dto.setDeductibles(salary.getDeductibles());
        return dto ;
    }

    /**
     * Updates the Salary entity based on the provided SalaryRequestDTO.
     *
     * @param salary the Salary entity to be updated
     * @param salaryRequestDTO the SalaryRequestDTO containing new data
     */
    private void updateSalaryFromDTO(Salary salary, SalaryRequestDTO salaryRequestDTO) {
        salary.setHoursWorked(salaryRequestDTO.getHoursWorked());
        salary.setPayRate(salaryRequestDTO.getPayRate());
        salary.setBonusPay(salaryRequestDTO.getBonusPay());
        salary.setDeductibles(salaryRequestDTO.getDeductibles());
        salary.setGrossPay(salary.calculateGrossPay());
        salary.setTakeHomePay(salary.calculateTakeHomePay());
    }

}
