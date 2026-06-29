package com.sc.demo.controller;

import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TokenController{

    @Autowired
    private TokenService tokenService;

    // تخزين Token
    @PostMapping("/V1/api/sc/saveToken")
    public long saveToken(@RequestBody TokenRequest tokenRequest){
        return tokenService.saveToken(tokenRequest);
    }
}
