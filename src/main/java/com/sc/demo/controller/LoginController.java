package com.sc.demo.controller;


import com.sc.demo.model.dto.*;
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
    public LogInResponse1 login(@RequestParam long phone_Number,
                                @RequestParam String country_code) {
        return loginService.logIn(phone_Number, country_code);
    }

    // جلب ال OTP بعد خزنه بالجدول
//    @GetMapping("/V1/api/sc/VerificationLoginApp")
//    public Long VerificationLoginApp(@RequestParam Long UserId,
//                                    @RequestParam Integer sendingType,
//                                    @RequestParam String Mobile){
//        return loginService.GeneratingVerificationLogin(UserId, sendingType, Mobile);
//    }
}
