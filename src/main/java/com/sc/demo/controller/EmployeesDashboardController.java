package com.sc.demo.controller;

import com.sc.demo.model.dto.employees.EmployeesRequest;
import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.service.employees.EmployeesDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeesDashboardController {

    @Autowired
    private EmployeesDashboardService employeesDashboardService;

    // اضافة مستخدمي الداشبورد
    @PostMapping("/V1/api/sc/newDashboardAccess")
    public Boolean addEmployees(@RequestBody EmployeesResponse employeesResponse){
        return employeesDashboardService.addEmployees(employeesResponse);
    }

    // جلب الموضفين المضافين
    @GetMapping("/V1/api/sc/employeesDashboard")
    public List<EmployeesRequest> employeesDashboard(){
        return employeesDashboardService.viewEmployees();
    }

}
