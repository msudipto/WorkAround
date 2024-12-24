package coms309.repository;

import coms309.entity.Employee;
import coms309.entity.LeaveRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestsRepository extends JpaRepository<LeaveRequests, Long> {
    List<LeaveRequests> findByEmployee(Employee employee);
    List<LeaveRequests> findByApprovalStatus(String approvalStatus);
}
