package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsRequest;
import com.sc.demo.model.notification.SendingType;
import com.sc.demo.service.announcements.AnnouncementsService;
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
public class DashAnnouncementsController implements SecuredRestController {

    @Autowired
    private AnnouncementsService announcementsService;

    @Value("${ATTACHMENT_PATH_ANNOUNCEMENTS}")
    private String uploadDir;

    // انشار تبليغ
    @PostMapping("/V1/api/sc/createAnnouncements")
    public Announcements createAnnouncements(@RequestParam String title,
                                             @RequestParam String description,
                                             @RequestParam(required = false) String branches,
                                             @RequestParam SendingType sendingType,
                                             @RequestParam(required = false) List<Long> userId,
                                             @RequestParam(required = false) MultipartFile file,
                                             @RequestHeader(name = "authorization") String token)  {
        return announcementsService.createAnnouncements(new AnnouncementsRequest
                (title, description, branches, sendingType, null), file, userId, token);
    }

    //  المرفقات مع التبليغات الداشبورد
    @GetMapping("/V1/api/sc/allAnnouncementsPhotos/{filename:.+}")
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

    // جميع تبليغات الداش بورد
    @GetMapping("/V1/api/sc/getAllAnnouncementsFamily")
    public List<AllAnnouncementsFamilyRequest> getAllAnnouncementsFamily(){
        return announcementsService.AllAnnouncementsFamily();
    }

    // حذف تبليغ
    @DeleteMapping("/V1/api/sc/deleteAnnouncement")
    public Boolean deleteAnnouncement(@RequestParam Long announcementsId){
        return announcementsService.deleteAnnouncement(announcementsId);
    }

    // تعديل تبليغ
    @PutMapping("/V1/api/sc/editAnnouncement")
    public Boolean editAnnouncement(@RequestParam Long announcementId,
                                          @RequestParam(required = false) String title,
                                          @RequestParam(required = false) String description,
                                          @RequestParam(required = false) MultipartFile file,
                                          @RequestHeader(name = "authorization") String token){
        return announcementsService.editAnnouncement(announcementId, title, description, file, token);
    }

    // تثبيت او الغاء تثبيت التبليغ
    @PutMapping("/V1/api/sc/editAnnouncementPin")
    public Boolean editAnnouncementPin(@RequestParam Long announcementId,
                                       @RequestHeader(name = "authorization") String token){
        return announcementsService.editAnnouncementPin(announcementId, token);
    }

}
