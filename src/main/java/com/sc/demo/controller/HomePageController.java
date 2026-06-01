package com.sc.demo.controller;

import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.homePage.HomePageRequest;
import com.sc.demo.model.homePage.LinkType;
import com.sc.demo.service.homePage.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    // اضافة صورة مربوطة بصفحة او موقع
    @PostMapping("/V1/api/sc/addHomePagePhoto")
    public String addHomePagePhoto(@RequestParam String link,
                                   @RequestParam LinkType linkType,
                                   @RequestParam (required = false) MultipartFile file){
        return homePageService.addHomePagePhoto(link,linkType, file);
    }
}
