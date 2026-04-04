package com.sc.demo.controller;


import com.sc.demo.model.users.dto.CreateUser;
import com.sc.demo.service.signup.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {


    @Autowired
    private SignupService signupService;


    @PostMapping("/V1/api/sc/signup")
    public long signup(@RequestBody CreateUser createUser) {
        return signupService.signup(createUser);
    }



}
