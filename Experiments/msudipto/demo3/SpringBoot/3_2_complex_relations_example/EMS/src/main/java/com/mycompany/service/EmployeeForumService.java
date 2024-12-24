package com.mycompany.service;

import com.mycompany.model.EmployeeForum;
import com.mycompany.repository.EmployeeForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeForumService {

    private final EmployeeForumRepository employeeForumRepository;

    // Constructor-based injection
    @Autowired
    public EmployeeForumService(EmployeeForumRepository employeeForumRepository) {
        this.employeeForumRepository = employeeForumRepository;
    }

    public List<EmployeeForum> getAllForumsForCompany(long id) {
        return employeeForumRepository.findByCompany_Id(id);
    }

    public List<EmployeeForum> getAllForumPerEmployee(long c_id, long e_id) {
        return employeeForumRepository.findByCompany_IdAndEmployees_Id(c_id, e_id);
    }
}