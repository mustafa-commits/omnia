package com.sc.demo.controller;

import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.service.homePage.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    // اضافة صورة مربوطة بصفحة او موقع
//    @PostMapping("/V1/api/sc/addHomePagePhoto")
//    public AppChatMaster addHomePagePhoto(){
//        return homePageService.addHomePagePhoto();
//    }
}
