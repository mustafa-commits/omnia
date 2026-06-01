package com.sc.demo.service.homePage;

import com.sc.demo.model.dto.homePage.HomePageRequest;
import com.sc.demo.model.homePage.HomePagePhoto;
import com.sc.demo.model.homePage.LinkType;
import com.sc.demo.repository.homePage.PhotoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HomePageService {

    @Autowired
    private PhotoRepo photoRepo;

    public String addHomePagePhoto(String link, LinkType linkType, MultipartFile file){

//        HomePagePhoto homePagePhoto = new HomePagePhoto(link, linkType, file);
//        homePagePhoto = photoRepo.save(homePagePhoto);

        return "تم,  اضافة الصورة مع الرابط";
    }
}
