package com.sc.demo.controller;

import com.sc.demo.model.dto.Login.LogInResponse;
import com.sc.demo.service.Login.LoginService;
import com.sc.demo.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> ChekLogin(@RequestParam Long phone_Number,
                                       @RequestParam Long secretCode){
        return loginService.ChekLoginApp(phone_Number, secretCode);
    }
}
