package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.DashboardUser.DashboardUserRequest;
import com.sc.demo.model.dto.DashboardUser.UserDashboardResponse;
import com.sc.demo.model.dto.token.TokenLoginRequest;
import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.service.userDashboard.UsersDashboardService;
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
    public Boolean newDashboardUser(@RequestBody UserDashboardResponse userDashboardResponse,
                                    @RequestHeader(name = "authorization") String token){
        return usersDashboardService.newUserDashboard(userDashboardResponse, token);
    }

    // جلب الموضفين المضافين
    @GetMapping("/V1/api/sc/usersDashboard")
    public List<DashboardUserRequest> usersDashboard(){
        return usersDashboardService.viewDashboardUser();
    }

    // تأكد من تسجيل دخول المستخدم الى الداش بورد
    @PostMapping("/V1/api/sc/loginUserDashboard")
    public TokenLoginRequest loginUserDashboard(@RequestParam String userName,
                                                @RequestParam String password){
        return usersDashboardService.loginUserDashboard(userName, password);
    }
}
