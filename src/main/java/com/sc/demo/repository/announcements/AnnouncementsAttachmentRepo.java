package com.sc.demo.repository.announcements;

import com.sc.demo.model.announcements.announcementsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsAttachmentRepo extends JpaRepository<announcementsAttachment, Long> {
}
