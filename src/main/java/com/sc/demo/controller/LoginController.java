package com.sc.demo.controller;


import com.google.api.Authentication;
import com.sc.demo.model.dto.Login.ChekLoginResponse;
import com.sc.demo.model.dto.Login.LogInResponse;
import com.sc.demo.service.Login.LoginService;
import com.sc.demo.service.Login.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;


    private final TokenService tokenService;

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    // تسجيل الدخول من خلال رقم الهاتف
    @GetMapping("/V1/api/sc/loginByPhone")
    public LogInResponse login(@RequestParam long phone_Number,
                               @RequestParam String country_code,
                               @RequestParam String birthDate) {
        return loginService.logIn(phone_Number, country_code, birthDate);
    }

    // جلب ال OTP بعد خزنه بالجدول
    @PostMapping("/V1/api/sc/ChekLogin")
    public ChekLoginResponse ChekLogin(@RequestParam Long phone_Number,
                                       @RequestParam Long secretCode){
        return loginService.ChekLoginApp(phone_Number, secretCode);
    }
}
