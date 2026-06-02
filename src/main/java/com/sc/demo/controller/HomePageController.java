package com.sc.demo.controller;

import com.sc.demo.model.dto.homePage.HomePageResponse;
import com.sc.demo.model.homePage.LinkType;
import com.sc.demo.service.homePage.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    // اضافة صورة مربوطة بصفحة او موقع
    @PostMapping("/V1/api/sc/addHomePagePhoto")
    public String addHomePagePhoto(@RequestParam String link,
                                   @RequestParam LinkType linkType,
                                   @RequestParam MultipartFile file,
                                   @RequestHeader(name = "authorization") String token){
        return homePageService.addHomePagePhoto(linkType, link, file, token);
    }

    // عرض الصور في واجهة التطبيق
    @GetMapping("/V1/sc/api/viewHomePagePhotos")
    public List<HomePageResponse> viewHomePagePhotos(@RequestHeader (name = "authorization") String token){
        return homePageService.viewHomePagePhotos(token);
    }
}
