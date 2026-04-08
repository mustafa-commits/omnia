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
    public Announcements createAnnouncements(AnnouncementsRequest announcementsRequest, MultipartFile file){
        Announcements announcements = new Announcements(announcementsRequest.sendId(),
                announcementsRequest.title(), announcementsRequest.description());
        announcements = announcementsRepo.save(announcements);
        for (AnnouncementsDetails a : announcementsRequest.announcementsDetails()){
            announcementsDetailsRepo.save(new AnnouncementsDetails(a.getUser_id(), announcements));
        }
        try {
            file.transferTo(new File("put path here"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return announcements;
    }


    // اشعارات التطيق لكل يززر
    public PHoneAnnouncements PHoneAnnouncements(long user_id) {
        Optional<PHoneAnnouncements> pNote = jdbcClient.sql("""
                   select a.CREATE_DATE as createDate, a.TITLE, a.DESCRIPTION
                   from SC_ANNOUNCEMENTS a
                   join SC_ANNOUNCEMENTS_DETAILS ad on a.ANNOUNCEMENTS_ID = ad.ANNOUNCEMENTS_ID
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
