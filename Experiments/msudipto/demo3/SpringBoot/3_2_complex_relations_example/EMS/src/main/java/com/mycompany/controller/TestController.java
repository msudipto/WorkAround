package com.mycompany.controller;

import com.mycompany.model.*;
import com.mycompany.repository.CompanyRepository;
import com.mycompany.repository.EmployeeForumRepository;
import com.mycompany.repository.EmployeeRepository;
import com.mycompany.repository.UserRepository;
import com.mycompany.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserRepository userRepository;
    private final EmployeeForumRepository employeeForumRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    // Constructor-based injection
    @Autowired
    public TestController(UserRepository userRepository,
                          EmployeeForumRepository employeeForumRepository,
                          CompanyRepository companyRepository,
                          EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeForumRepository = employeeForumRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = "/enter", produces = "application/json")
    public Message enterDummyData() {

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("here");

        // Save initial User data
        userRepository.save(new User(1, "bvivek2@iastate.edu", "password", "Vivek", Role.ADMIN));

        // Save initial Company data
        companyRepository.save(new Company(1, "companyA", "C1"));
        companyRepository.save(new Company(2, "companyB", "C2"));

        Company c1 = companyRepository.findById(1);
        Company c2 = companyRepository.findById(2);

        // Save initial EmployeeForum data
        employeeForumRepository.save(new EmployeeForum(1, "Dark Humour", c1));
        employeeForumRepository.save(new EmployeeForum(2, "Foodies", c1));
        employeeForumRepository.save(new EmployeeForum(3, "Something Boring", c2));
        employeeForumRepository.save(new EmployeeForum(4, "Running out of Names now", c2));

        EmployeeForum ef1 = employeeForumRepository.findById(1);
        EmployeeForum ef2 = employeeForumRepository.findById(2);
        EmployeeForum ef3 = employeeForumRepository.findById(3);
        EmployeeForum ef4 = employeeForumRepository.findById(4);

        // Save initial Employee data
        employeeRepository.save(new Employee(1, "A", "a@z.com", "addr", c1));
        employeeRepository.save(new Employee(2, "B", "b@z.com", "bddr", c1));
        employeeRepository.save(new Employee(3, "C", "c@z.com", "cddr", c1));
        employeeRepository.save(new Employee(4, "D", "d@z.com", "dddr", c1));
        employeeRepository.save(new Employee(5, "E", "e@z.com", "eddr", c2));
        employeeRepository.save(new Employee(6, "F", "f@z.com", "fddr", c2));
        employeeRepository.save(new Employee(7, "G", "g@z.com", "gddr", c2));
        employeeRepository.save(new Employee(8, "H", "h@z.com", "hddr", c2));

        Employee e1 = employeeRepository.findById(1);
        Employee e2 = employeeRepository.findById(2);
        Employee e3 = employeeRepository.findById(3);
        Employee e4 = employeeRepository.findById(4);
        Employee e5 = employeeRepository.findById(5);
        Employee e6 = employeeRepository.findById(6);
        Employee e7 = employeeRepository.findById(7);
        Employee e8 = employeeRepository.findById(8);

        // Establish relationships between Employees and EmployeeForums
        e1.addEmployeeForum(ef1);
        ef1.addEmployee(e1);

        e1.addEmployeeForum(ef2);
        ef2.addEmployee(e1);

        e2.addEmployeeForum(ef2);
        ef2.addEmployee(e2);

        e3.addEmployeeForum(ef1);
        ef1.addEmployee(e3);

        e4.addEmployeeForum(ef1);
        ef1.addEmployee(e4);

        e4.addEmployeeForum(ef2);
        ef2.addEmployee(e4);

        e5.addEmployeeForum(ef3);
        ef3.addEmployee(e5);

        e5.addEmployeeForum(ef4);
        ef4.addEmployee(e5);

        e6.addEmployeeForum(ef4);
        ef4.addEmployee(e6);

        e7.addEmployeeForum(ef3);
        ef3.addEmployee(e7);

        e8.addEmployeeForum(ef3);
        ef3.addEmployee(e8);

        e8.addEmployeeForum(ef4);
        ef4.addEmployee(e8);

        // Save updated Employee data
        employeeRepository.save(e1);
        employeeRepository.save(e2);
        employeeRepository.save(e3);
        employeeRepository.save(e4);
        employeeRepository.save(e5);
        employeeRepository.save(e6);
        employeeRepository.save(e7);
        employeeRepository.save(e8);

        return new Message("Entered All Data", 0);
    }
}