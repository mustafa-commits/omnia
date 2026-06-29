package com.sc.demo.repository.announcements;

import com.sc.demo.model.announcement.Announcements;
import com.sc.demo.model.announcement.AnnouncementsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsAttachmentRepo extends JpaRepository<AnnouncementsAttachment, Long> {
    AnnouncementsAttachment findByAnnouncements(
            Announcements announcement
    );

}
