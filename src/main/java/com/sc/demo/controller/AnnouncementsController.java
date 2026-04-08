package com.sc.demo.controller;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.dto.AllAnnouncementsFamily;
import com.sc.demo.model.dto.AnnouncementsRequest;
import com.sc.demo.model.dto.PHoneAnnouncements;
import com.sc.demo.service.Announcements.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AnnouncementsController {

    @Autowired
    private AnnouncementsService announcementsService;

    // انشار تبليغ
    @PostMapping("/V1/api/sc/createAnnouncements")
    public Announcements createAnnouncements(@RequestParam Integer sendId,
                                             @RequestParam String title,
                                             @RequestParam String description,
                                             @RequestParam List<AnnouncementsDetails> announcementsDetails,
                                             @RequestParam MultipartFile file){
        return announcementsService.createAnnouncements(new AnnouncementsRequest(sendId, title, description, announcementsDetails), file);
    }

    // تبليغات التلفون
    @GetMapping("/V1/api/sc/getPHoneAnnouncements")
    public PHoneAnnouncements getPHoneAnnouncements(@RequestParam long user_id){
        return announcementsService.PHoneAnnouncements(user_id);
    }

    // جميع تبليغات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllAnnouncementsFamily")
    public AllAnnouncementsFamily getAllAnnouncementsFamily(@RequestParam long user_id){
        return announcementsService.AllAnnouncementsFamily(user_id);
    }
}
