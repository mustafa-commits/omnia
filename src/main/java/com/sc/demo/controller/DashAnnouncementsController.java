package com.sc.demo.controller;

import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.service.announcements.AnnouncementsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DashAnnouncementsController {

    @Autowired
    private AnnouncementsService announcementsService;

    String uploadDir = "http://10.76.233.71:1801/socialCare";

    //  المرفقات مع التبليغات الداشبورد
    @GetMapping("/V1/api/sc/getAllAnnouncementsFamily/{filename:.+}")
    public void serveAllFile(
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

    // جميع تبليغات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllAnnouncementsFamily")
    public List<AllAnnouncementsFamilyRequest> getAllAnnouncementsFamily(){
        return announcementsService.AllAnnouncementsFamily();
    }
}
