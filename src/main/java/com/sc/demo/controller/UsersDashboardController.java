package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.employees.EmployeesRequest;
import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.model.dto.login.GetUserIdWithToken;
import com.sc.demo.service.employees.UsersDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UsersDashboardController implements SecuredRestController {

    @Autowired
    private UsersDashboardService usersDashboardService;

    // اضافة مستخدمي الداش بورد
    @PostMapping("/V1/api/sc/newDashboardUser")
    public Boolean newDashboardUser(@RequestBody EmployeesResponse employeesResponse,
                                    @RequestHeader(name = "authorization") String token){
        return usersDashboardService.newDashboardUser(employeesResponse, token);
    }

    // جلب الموضفين المضافين
    @GetMapping("/V1/api/sc/usersDashboard")
    public List<EmployeesRequest> usersDashboard(){
        return usersDashboardService.viewEmployees();
    }

    // تأكد من تسجيل دخول المستخدم الى الداش بورد
    @PostMapping("/V1/api/sc/loginEmployee")
    public GetUserIdWithToken loginEmployee(@RequestParam String userName,
                                            @RequestParam String password){
        return usersDashboardService.loginEmployee(userName, password);
    }
}
