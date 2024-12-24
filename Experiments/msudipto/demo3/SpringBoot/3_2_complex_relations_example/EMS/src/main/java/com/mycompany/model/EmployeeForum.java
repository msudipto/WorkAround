package com.mycompany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class EmployeeForum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date createdOn;

    @Column(nullable = false)
    private Date modifiedOn;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false) // Proper mapping for company_id
    @JsonIgnore
    private Company company;

    @ManyToMany(mappedBy = "employeeForums")
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    // Constructor with parameters
    public EmployeeForum(long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.createdOn = new Date();
        this.modifiedOn = new Date();
        this.company = company;
    }

    // No-arg constructor
    public EmployeeForum() {
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    // Getters and Setters
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }
}