package com.sc.demo.controller;


import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.dto.AppUserRequest;
import com.sc.demo.model.dto.AppUserRequest2;
import com.sc.demo.model.dto.LoginResponse;
import com.sc.demo.service.users.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/V1/api/sc/loginById")
    public AppUser login(@RequestBody long user_id) {
        return loginService.login(user_id);
    }

    @PostMapping("/V2/api/sc/loginById")
    public AppUser login2(@RequestParam long user_id) {
        return loginService.login(user_id);
    }

    @GetMapping("/V3/api/sc/loginById")
    public AppUser login3(@RequestParam long user_id) {
        System.out.println(user_id);
        return loginService.login(user_id);
    }

    @PostMapping("/V4/api/sc/loginById")
    public AppUser login4(@RequestBody AppUserRequest data) {
        return loginService.login(data.user_id());
    }

    @PostMapping("/V5/api/sc/loginById")
    public String login5(@RequestBody AppUserRequest data) {
        AppUser user1;
        user1= loginService.login(data.user_id());
        return user1.getHeadFamilyName();
    }

    @PostMapping("/V6/api/sc/loginById")
    public AppUser login6(@RequestBody AppUserRequest data) {
        return loginService.login2(data.user_id());
    }

    @PostMapping("/V7/api/sc/loginById")
    public LoginResponse login7(@RequestBody AppUserRequest data) {
        return loginService.login3(data.user_id());
    }

    @PostMapping("/V1/api/sc/loginByPhone")
    public LoginResponse login7(@RequestBody AppUserRequest2 data) {
        return loginService.login4(data);
    }


}
