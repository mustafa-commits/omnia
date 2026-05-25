package com.sc.demo.controller;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.dto.Announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.Announcements.AnnouncementsRequest;
import com.sc.demo.model.dto.Announcements.PHoneAnnouncementsRequest;
import com.sc.demo.service.announcements.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AnnouncementsController {

    @Autowired
    private AnnouncementsService announcementsService;

    // انشار تبليغ
    @PostMapping("/V1/api/sc/createAnnouncements")
    public Announcements createAnnouncements(@RequestParam Integer sendId,
                                             @RequestParam String title,
                                             @RequestParam String description,
                                             @RequestHeader(name = "authorization", required = false) String token,
                                             @RequestParam (required = false) MultipartFile file)  {
        return announcementsService.createAnnouncements(new AnnouncementsRequest
                (sendId, title, description, null), file, token);
    }

    // تبليغات التلفون
    @GetMapping("/V1/api/sc/getPHoneAnnouncements")
    public List<PHoneAnnouncementsRequest> getPHoneAnnouncements(String token){
        return announcementsService.PHoneAnnouncements(token);
    }

    // جميع تبليغات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllAnnouncementsFamily")
    public List<AllAnnouncementsFamilyRequest> getAllAnnouncementsFamily(){
        return announcementsService.AllAnnouncementsFamily();
    }
}
