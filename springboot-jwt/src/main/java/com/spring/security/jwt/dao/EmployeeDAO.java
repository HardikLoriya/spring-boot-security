package com.spring.security.jwt.dao;

import org.springframework.stereotype.Repository;

import com.spring.security.jwt.models.Employee;
import com.spring.security.jwt.models.Employees;

@Repository
public class EmployeeDAO 
{
    private static Employees list = new Employees();
    
    static 
    {
        list.getEmployeeList().add(new Employee(1, "Hardik", "Loriya", "hloriya@gmail.com"));
        list.getEmployeeList().add(new Employee(2, "Mehul", "Patel", "abc@gmail.com"));
        list.getEmployeeList().add(new Employee(3, "Dhruv", "Loriya", "dloriya@gmail.com"));
    }
    
    public Employees getAllEmployees() 
    {
        return list;
    }
    
    public void addEmployee(Employee employee) {
        list.getEmployeeList().add(employee);
    }
}
