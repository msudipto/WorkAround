package coms309.repository;

import coms309.entity.Employee;
import coms309.entity.Tasks;
import coms309.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public  interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByEmployeeAndLogDate(Employee employee, Date logDate);
    List<TimeLog> findByLogDateAndClockOutTimeIsNull(Date logDate);
    List<TimeLog> findByLogDate(Date logDate);
}
