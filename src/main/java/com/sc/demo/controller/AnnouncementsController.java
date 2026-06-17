package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.announcements.AnnouncementsTokenRequest;
import com.sc.demo.model.dto.announcements.PhoneAnnouncementsRequest;
import com.sc.demo.model.dto.chat.ChatTokenRequest;
import com.sc.demo.service.announcements.AnnouncementsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AnnouncementsController implements SecuredRestController {

    @Autowired
    private AnnouncementsService announcementsService;

    @Value("${ATTACHMENT_PATH_ANNOUNCEMENTS}")
    private String uploadDir;

    //  المرفقات مع التبليغات التلفون
    @GetMapping("/V1/api/sc/photoAnnouncements/{filename:.+}")
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

    // تبليغات التلفون
    @GetMapping("/V1/api/sc/getPhoneAnnouncements")
    public List<PhoneAnnouncementsRequest> getPhoneAnnouncements(@RequestHeader(name = "authorization") String token){
        return announcementsService.PhoneAnnouncements(token);
    }

    // تخزين Token
    @PostMapping("/V1/api/sc/setAnnouncementsToken")
    public long setAnnouncementsToken(@RequestBody AnnouncementsTokenRequest announcementsTokenRequest){
        return announcementsService.saveToken(announcementsTokenRequest);
    }
}
