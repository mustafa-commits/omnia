package com.sc.demo.service.homePage;

import com.sc.demo.model.announcements.AnnouncementsAttachment;
import com.sc.demo.model.dto.homePage.HomePageRequest;
import com.sc.demo.model.homePage.HomePagePhoto;
import com.sc.demo.model.homePage.LinkType;
import com.sc.demo.repository.homePage.PhotoRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class HomePageService {

    @Autowired
    private PhotoRepo photoRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Environment environment;

    public String addHomePagePhoto(LinkType linkType, String link, MultipartFile file, String token){

        var userId = tokenService.decodeToken(token.substring(7)).getSubject();

//        HomePagePhoto homePagePhoto = new HomePagePhoto(homePageRequest.linkType(), homePageRequest.link());

                String originalFilename = file.getOriginalFilename();
                String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
                String filePath = environment.getProperty("ATTACHMENT_PATH") + newFilename;
                photoRepo.save(new HomePagePhoto(newFilename, linkType, link));

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return "تم,  اضافة الصورة مع الرابط";
    }
}
