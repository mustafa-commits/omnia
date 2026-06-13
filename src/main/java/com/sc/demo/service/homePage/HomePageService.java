package com.sc.demo.service.homePage;

import com.sc.demo.model.dto.homePage.homePageResponse;
import com.sc.demo.model.homePage.homePagePhoto;
import com.sc.demo.model.homePage.linkType;
import com.sc.demo.repository.homePage.PhotoRepo;
import com.sc.demo.service.token.TokenService;
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

    public String addHomePagePhoto(linkType linkType, String link, MultipartFile file){

                String originalFilename = file.getOriginalFilename();
                String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
                String filePath = environment.getProperty("ATTACHMENT_PATH_HOMEPAGE") + newFilename;
        Long createBy = null;
        photoRepo.save(new homePagePhoto(newFilename, linkType, link, createBy));

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "تم اضافة الصورة للواجهة الرئيسية";
    }

    public List<homePageResponse> viewHomePagePhotos() {
        return jdbcClient.sql("""
                   SELECT TO_CHAR(:path) || FILE_NAME AS fileName ,
                           LINK_TYPE AS linkType,
                           LINK AS link
                   FROM MOBAPP.SC_HOMEPAGE_PHOTO
                   ORDER BY CREATE_DATE DESC
                   FETCH FIRST 3 ROWS ONLY
                """)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/homePagePhotos/")
                .query(homePageResponse.class)
                .list();

    }
}
