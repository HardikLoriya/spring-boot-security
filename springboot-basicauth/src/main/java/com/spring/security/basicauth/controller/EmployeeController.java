package com.spring.security.basicauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.basicauth.dao.EmployeeDAO;
import com.spring.security.basicauth.model.Employee;
import com.spring.security.basicauth.model.Employees;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
	@Autowired
	private EmployeeDAO employeeDao;

	@GetMapping(path = "/", produces = "application/json")
	public Employees getEmployees() {
		return employeeDao.getAllEmployees();
	}

	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> addEmployee(@RequestBody Employee employee) throws Exception {
		Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
		employee.setId(id);

		employeeDao.addEmployee(employee);

		return new ResponseEntity<>(employee, HttpStatus.CREATED);
	}
}
