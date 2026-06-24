package com.sc.demo.controller;

import com.sc.demo.model.dto.employees.EmployeesRequest;
import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.model.dto.login.GetUserIdWithToken;
import com.sc.demo.service.employees.EmployeesDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class EmployeesDashboardController {

    @Autowired
    private EmployeesDashboardService employeesDashboardService;

    // اضافة مستخدمي الداش بورد
    @PostMapping("/V1/api/sc/newDashboardAccess")
    public Boolean addEmployees(@RequestBody EmployeesResponse employeesResponse,
                                @RequestHeader(name = "authorization") String token){
        return employeesDashboardService.addEmployees(employeesResponse, token);
    }

    // جلب الموضفين المضافين
    @GetMapping("/V1/api/sc/employeesDashboard")
    public List<EmployeesRequest> employeesDashboard(){
        return employeesDashboardService.viewEmployees();
    }

    // تأكد من تسجيل دخول المستخدم الى الداش بورد
    @PostMapping("/V1/api/sc/loginEmployee")
    public GetUserIdWithToken loginEmployee(@RequestParam String userName,
                                            @RequestParam String password){
        return employeesDashboardService.loginEmployee(userName, password);
    }
}
