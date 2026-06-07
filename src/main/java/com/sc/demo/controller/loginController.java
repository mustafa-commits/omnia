package com.sc.demo.controller;

import com.sc.demo.model.dto.familyInfo.AppUserRequest;
import com.sc.demo.model.dto.login.LogInResponse;
import com.sc.demo.service.login.LoginService;
import com.sc.demo.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class loginController {

    @Autowired
    private LoginService loginService;

    private final TokenService tokenService;

    public loginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // تسجيل الدخول من خلال رقم الهاتف
    @GetMapping("/V1/api/sc/loginByPhone")
    public List<LogInResponse> login(@RequestParam long phone_Number,
                                    @RequestParam String country_code,
                                    @RequestParam String birthDate) {
        return loginService.logIn(phone_Number, country_code, birthDate);
    }

    // جلب ال OTP بعد خزنه بالجدول
    @PostMapping("/V1/api/sc/ChekLogin")
    public ResponseEntity<?> ChekLogin(@RequestBody AppUserRequest appUserRequest){
        return loginService.ChekLoginApp(appUserRequest);
    }
}
