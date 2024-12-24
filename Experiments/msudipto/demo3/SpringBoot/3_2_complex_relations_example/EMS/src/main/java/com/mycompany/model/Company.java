package com.mycompany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private Date createdOn;

    @Column(nullable = false)
    private Date modifiedOn;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<EmployeeForum> employeeForums = new ArrayList<>();

    // Constructors
    public Company(long id, String name, String registrationNumber) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    public Company() {
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<EmployeeForum> getEmployeeForums() {
        return employeeForums;
    }

    public void addEmployeeForum(EmployeeForum employeeForum) {
        this.employeeForums.add(employeeForum);
    }
}