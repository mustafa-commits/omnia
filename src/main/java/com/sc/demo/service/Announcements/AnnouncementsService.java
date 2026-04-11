package com.sc.demo.service.Announcements;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.dto.AllAnnouncementsFamily;
import com.sc.demo.model.dto.AnnouncementsRequest;
import com.sc.demo.model.dto.PHoneAnnouncements;
import com.sc.demo.repository.AnnouncementsDetailsRepo;
import com.sc.demo.repository.AnnouncementsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementsService {

    @Autowired
    private AnnouncementsRepo announcementsRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AnnouncementsDetailsRepo announcementsDetailsRepo;

    // انشاء تبليغ
    public Announcements createAnnouncements(AnnouncementsRequest announcementsRequest, MultipartFile file, List<Long> user_id){
        Announcements announcements = new Announcements(announcementsRequest.sendId(),
                announcementsRequest.title(), announcementsRequest.description());
        announcements = announcementsRepo.save(announcements);
        if (user_id != null)
        for (Long a : user_id){
            announcementsDetailsRepo.save(new AnnouncementsDetails(a, announcements));
        }
        if (file != null)
        try {
            file.transferTo(new File("C:\\Users\\ASCF\\Desktop\\Omnia\\Attachments"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return announcements;
    }


    // اشعارات التطيق لكل يززر
    public PHoneAnnouncements PHoneAnnouncements(long user_id) {
        Optional<PHoneAnnouncements> pNote = jdbcClient.sql("""
                   select a.CREATE_DATE as createDate, a.TITLE, a.DESCRIPTION, ad.user_id, at.FILE_NAME
                   from SC_ANNOUNCEMENTS a
                   Left join SC_ANNOUNCEMENTS_DETAILS ad on a.ANNOUNCEMENTS_ID = ad.ANNOUNCEMENTS_ID
                   Left join sc_announcements_attachment at on a.ANNOUNCEMENTS_ID = at.ANNOUNCEMENTS_ID
                   Where ad.user_id = :user_id
                """).param("user_id",user_id).query(PHoneAnnouncements.class).optional();

        if (pNote.isPresent())
            return pNote.get();
        else
            return null;
    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public AllAnnouncementsFamily AllAnnouncementsFamily(long user_id) {

        Optional <AllAnnouncementsFamily>  allDNote = jdbcClient.sql("""
                   select a.CREATE_DATE as createDate, a.TITLE, a.DESCRIPTION
                   from SC_ANNOUNCEMENTS a
                   join SC_ANNOUNCEMENTS_DETAILS ad on a.ANNOUNCEMENTS_ID = ad.ANNOUNCEMENTS_ID
                   Where ad.user_id = :user_id
                """).param("user_id",user_id).query(AllAnnouncementsFamily.class).optional();

        if (allDNote.isPresent())
            return allDNote.get();
        else
            return null;
    }
}
