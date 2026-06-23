package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.homePage.homePageResponse;
import com.sc.demo.model.homePage.linkType;
import com.sc.demo.service.homePage.HomePageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class HomePageController implements SecuredRestController {

    @Autowired
    private HomePageService homePageService;

    @Value("${ATTACHMENT_PATH_HOMEPAGE}")
    private String uploadDir;

    // اضافة صورة مربوطة بصفحة او موقع
    @PostMapping("/V1/api/sc/addHomePagePhoto")
    public String addHomePagePhoto(@RequestParam String link,
                                   @RequestParam linkType linkType,
                                   @RequestParam MultipartFile file,
                                   @RequestHeader(name = "authorization") String token){
        return homePageService.addHomePagePhoto(linkType, link, file, token);
    }

    //  المرفقات في الواجهة
    @GetMapping("/V1/api/sc/homePagePhotos/{filename:.+}")
    public void serveFile(
            @PathVariable String filename,
            HttpServletResponse response
    ) throws IOException {
        var file = Paths.get(uploadDir, filename);
        if (Files.notExists(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(Files.probeContentType(file));
        response.setContentLengthLong(Files.size(file));
        Files.copy(file, response.getOutputStream());
    }

    // عرض الصور في واجهة التطبيق
    @GetMapping("/V1/api/sc/viewHomePagePhotos")
    public List<homePageResponse> viewHomePagePhotos(){
        return homePageService.viewHomePagePhotos();
    }
}
