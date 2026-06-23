package com.sc.demo.service.homePage;

import com.sc.demo.model.dto.homePage.homePageResponse;
import com.sc.demo.model.homePage.homePagePhoto;
import com.sc.demo.model.homePage.linkType;
import com.sc.demo.repository.homePage.PhotoRepo;
import com.sc.demo.service.token.TokenService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class HomePageService {

    @Autowired
    private PhotoRepo photoRepo;

    @Autowired
    private Environment environment;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private TokenService tokenService;

    public String addHomePagePhoto(linkType linkType, String link, MultipartFile file, String token){
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();

        String originalFilename = file.getOriginalFilename();
        String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = environment.getProperty("ATTACHMENT_PATH_HOMEPAGE") + newFilename;

        photoRepo.save(new homePagePhoto(newFilename, linkType, link, Long.parseLong(employeesId)));

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "تم اضافة الصورة للواجهة الرئيسية";
    }

    // جلب الصورد في الشاشة الرئيسية
    public List<homePageResponse> viewHomePagePhotos() {
        return jdbcClient.sql("""
                 SELECT PHOTO_ID AS photoId
                        ,LINK_TYPE AS linkType
                        ,TO_CHAR(:path) || FILE_NAME AS fileName
                        ,LINK AS link
                 FROM MOBAPP.SC_HOMEPAGE_PHOTOS
                 ORDER BY CREATE_DATE DESC
                """)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/homePagePhotos/")
                .query(homePageResponse.class)
                .list();

    }
}
