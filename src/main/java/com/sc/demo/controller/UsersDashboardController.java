package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.usersDashboard.UsersDashboardRequest;
import com.sc.demo.model.dto.usersDashboard.UserDashboardResponse;
import com.sc.demo.model.dto.login.LoginRequest2;
import com.sc.demo.service.userDashboard.UsersDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UsersDashboardController implements SecuredRestController {

    @Autowired
    private UsersDashboardService usersDashboardService;

    // اضافة مستخدمي الداش بورد
    @PostMapping("/V1/api/sc/newUsersDashboard")
    public Boolean newUsersDashboard(@RequestBody UserDashboardResponse userDashboardResponse,
                                    @RequestHeader(name = "authorization") String token){
        return usersDashboardService.newUserDashboard(userDashboardResponse, token);
    }

    // جلب الموضفين المضافين
    @GetMapping("/V1/api/sc/usersDashboard")
    public List<UsersDashboardRequest> usersDashboard(){
        return usersDashboardService.viewUsersDashboard();
    }

    // تأكد من تسجيل دخول المستخدم الى الداش بورد
    @PostMapping("/V1/api/sc/loginUserDashboard")
    public ResponseEntity<LoginRequest2> loginUserDashboard(@RequestParam String userName,
                                                           @RequestParam String password){
        return usersDashboardService.loginUserDashboard(userName, password);
    }
}
