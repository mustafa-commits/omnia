package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.Branches;
import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsRequest;
import com.sc.demo.model.dto.announcements.PhoneAnnouncementsRequest;
import com.sc.demo.model.notification.SendingType;
import com.sc.demo.service.announcements.AnnouncementsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class announcementsController implements SecuredRestController {

    @Autowired
    private AnnouncementsService announcementsService;

    // انشار تبليغ
    @PostMapping("/V1/api/sc/createAnnouncements")
    public Announcements createAnnouncements(@RequestParam Integer sendId,
                                             @RequestParam String title,
                                             @RequestParam String description,
                                             @RequestParam Branches branches,
                                             @RequestParam SendingType sendingType,
                                             @RequestParam List<Long> userId,
                                             @RequestParam (required = false) MultipartFile file)  {
        return announcementsService.createAnnouncements(new AnnouncementsRequest
                (sendId, title, description, branches, sendingType, null), file, userId);
    }

    //  المرفقات مع التبليغات التلفون
    String uploadDir = "http://10.76.233.71:1801/socialCare";

    @GetMapping("/V1/api/sc/getPhoneAnnouncements/{filename:.+}")
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
