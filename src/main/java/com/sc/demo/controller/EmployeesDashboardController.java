package com.sc.demo.controller;

import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.service.employees.EmployeesDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeesDashboardController {

    @Autowired
    private EmployeesDashboardService employeesDashboardService;

    @PostMapping("/V1/api/sc/newDashboardAccess")
    public Boolean addEmployees(@RequestBody EmployeesResponse employeesResponse){
        return employeesDashboardService.addEmployees(employeesResponse);
    }

}
