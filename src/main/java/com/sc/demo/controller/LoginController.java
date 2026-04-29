package com.sc.demo.controller;


import com.sc.demo.model.dto.*;
import com.sc.demo.service.users.LoginService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    // تسجيل الدخول من خلال رقم الهاتف
    @GetMapping("/V1/api/sc/loginByPhone")
    public LogInResponse1 login(@RequestParam long phone_Number,
                                @RequestParam String country_code,
                                @RequestParam LocalDateTime birthDate) {
        return loginService.logIn(phone_Number, country_code, birthDate);
    }

    // جلب ال OTP بعد خزنه بالجدول
    @PostMapping("/V1/api/sc/ChekLogin")
    public ChekLoginResponse ChekLogin(@RequestParam Long phone_Number,
                                       @RequestParam Long secretCode){
        return loginService.ChekLoginApp(phone_Number, secretCode);
    }
}
