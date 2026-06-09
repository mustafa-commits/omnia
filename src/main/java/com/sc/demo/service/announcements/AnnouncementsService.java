package com.sc.demo.service.announcements;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsAttachment;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsRequest;
import com.sc.demo.model.dto.announcements.PhoneAnnouncementsRequest;
import com.sc.demo.repository.announcements.AnnouncementsAttachmentRepo;
import com.sc.demo.repository.announcements.AnnouncementsDetailsRepo;
import com.sc.demo.repository.announcements.AnnouncementsRepo;
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
public class AnnouncementsService {

    @Autowired
    private AnnouncementsRepo announcementsRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AnnouncementsDetailsRepo announcementsDetailsRepo;

    @Autowired
    private AnnouncementsAttachmentRepo announcementsAttachmentRepo;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenService tokenService;

    // انشاء تبليغ
    public Announcements createAnnouncements(AnnouncementsRequest announcementsRequest, MultipartFile file, String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userId);

        Announcements announcements = new Announcements(announcementsRequest.sendId(), announcementsRequest.title(), announcementsRequest.description());
        System.out.println(announcements);
        announcements = announcementsRepo.save(announcements);
        if (userId != null)
            announcementsDetailsRepo.save(new AnnouncementsDetails(Long.parseLong(userId), announcements));
        if (file != null) try {
            String originalFilename = file.getOriginalFilename();
            String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = environment.getProperty("ATTACHMENT_PATH_ANNOUNCEMENTS") + newFilename;
            announcementsAttachmentRepo.save(new AnnouncementsAttachment(newFilename, announcements));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return announcements;
    }

    // اشعارات التطيق لكل يززر
    public List<PhoneAnnouncementsRequest> PhoneAnnouncements(String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();

        return jdbcClient.sql("""
                   SELECT A.CREATE_DATE AS createDate
                           ,A.TITLE
                           ,A.DESCRIPTION
                           ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                   FROM SC_ANNOUNCEMENTS A
                   JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                   WHERE AD.USER_ID = :user_id OR AD.USER_ID = 0
                """)
                .param("user_id", userId)
                .param("path", "http://10.76.233.71:1801/V1/api/sc/getPhoneAnnouncements/")
                .query(PhoneAnnouncementsRequest.class)
                .list();

    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllAnnouncementsFamilyRequest> AllAnnouncementsFamily() {
        return jdbcClient.sql("""
                   SELECT A.CREATE_DATE AS createDate
                          ,A.TITLE
                          ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                          ,A.DESCRIPTION
                   FROM SC_ANNOUNCEMENTS A
                   JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                """)
                .param("path", "http://10.76.233.71:1801/V1/api/sc/getAllAnnouncementsFamily/")
                .query(AllAnnouncementsFamilyRequest.class)
                .list();

    }
}
