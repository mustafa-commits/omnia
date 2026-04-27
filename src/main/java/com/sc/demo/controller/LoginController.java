package com.sc.demo.controller;


import com.sc.demo.model.dto.*;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.service.users.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    // تسجيل الدخول من خلال رقم الهاتف
    @GetMapping("/V1/api/sc/loginByPhone")
    public LogInResponse1 login(@RequestParam String P_Phone_Number) {
        return loginService.logIn(P_Phone_Number);
    }

    // جلب ال OTP بعد خزنه بالجدول
    @GetMapping("/V1/api/sc/VerificationLoginApp")
    public VerificationLoginResponse VerificationLoginApp(@RequestParam String P_Phone_Number){
        return loginService.VerificationLoginApp(P_Phone_Number);
    }
}
