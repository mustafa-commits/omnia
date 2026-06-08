package com.sc.demo.service.announcements;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsAttachment;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.dto.announcements.allAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.announcementsRequest;
import com.sc.demo.model.dto.announcements.phoneAnnouncementsRequest;
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
    public Announcements createAnnouncements(announcementsRequest announcementsRequest, MultipartFile file, String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userId);

        Announcements announcements = new Announcements(announcementsRequest.sendId(), announcementsRequest.title(), announcementsRequest.description());
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
    public List<phoneAnnouncementsRequest> PHoneAnnouncements(String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();

        return jdbcClient.sql("""
                   select a.CREATE_DATE as createDate, a.TITLE, a.DESCRIPTION,
                   ad.user_id, at.FILE_NAME
                   from SC_ANNOUNCEMENTS a
                   Left join SC_ANNOUNCEMENTS_DETAILS ad on a.ANNOUNCEMENTS_ID = ad.ANNOUNCEMENTS_ID
                   Left join sc_announcements_attachment at on a.ANNOUNCEMENTS_ID = at.ANNOUNCEMENTS_ID
                   Where ad.user_id = :user_id OR ad.USER_ID = 0
                """).param("user_id", userId).query(phoneAnnouncementsRequest.class).list();

    }
    /*
    (  select json_arrayagg(
                json_object('img_name' is 'http://10.76.232.55:8090/i/ayn_hc/ayn_hic/hic_pharmacy_supply_order_files/' || pharmacy_supplier_files_id
                                          || '.' || substr (file_type, instr (file_type, '/', 1) + 1)
                           ,'full_img' is case when upper (substr (file_type, instr (file_type, '/', 1) + 1)) = 'pdf'
                                             then n'<img src="../i/ayn_hc/pdf.png" width="100" height="100" id="image1">'
                                             when upper (substr (file_type, 0, instr (file_type, '/', 1) - 1)) = 'image'
                                             then n'<img src="http://10.76.232.55:8090/i/ayn_hc/ayn_hic/hic_pharmacy_supply_order_files/' || pharmacy_supplier_files_id
                                                 || '.' || substr (file_type, instr (file_type, '/', 1) + 1) || '" width="100" height="100" id="image1">'
                                             else n'<img src="../i/ayn_hc/unknown.png" width="100" height="100" id="image1">'
                                      end) ) */

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<allAnnouncementsFamilyRequest> AllAnnouncementsFamily() {

        return jdbcClient.sql("""
                   select a.CREATE_DATE as createDate, a.TITLE, a.DESCRIPTION
                   from SC_ANNOUNCEMENTS a
                   join SC_ANNOUNCEMENTS_DETAILS ad on a.ANNOUNCEMENTS_ID = ad.ANNOUNCEMENTS_ID
                """).query(allAnnouncementsFamilyRequest.class).list();

    }
}
