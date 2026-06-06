package com.sc.demo.controller;

import com.sc.demo.model.dto.homePage.homePageResponse;
import com.sc.demo.model.homePage.linkType;
import com.sc.demo.service.homePage.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class homePageController {

    @Autowired
    private HomePageService homePageService;

    // اضافة صورة مربوطة بصفحة او موقع
    @PostMapping("/V1/api/sc/addHomePagePhoto")
    public String addHomePagePhoto(@RequestParam String link,
                                   @RequestParam linkType linkType,
                                   @RequestParam MultipartFile file){
        return homePageService.addHomePagePhoto(linkType, link, file);
    }

    // عرض الصور في واجهة التطبيق
    @GetMapping("/V1/sc/api/viewHomePagePhotos")
    public List<homePageResponse> viewHomePagePhotos(){
        return homePageService.viewHomePagePhotos();
    }
}
