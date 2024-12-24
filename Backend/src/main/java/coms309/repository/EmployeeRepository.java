package coms309.repository;

import coms309.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
