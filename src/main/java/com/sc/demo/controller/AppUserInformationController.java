package com.sc.demo.controller;

import com.sc.demo.model.dto.AppUserRequest;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.service.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserInformationController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/V1/sc/api/getUserInformation")
    public AppUser getUserInformation(@RequestBody AppUserRequest appUserRequest){
        return appUserService.getFamilyInfoInHomePage(appUserRequest);
    }
}
