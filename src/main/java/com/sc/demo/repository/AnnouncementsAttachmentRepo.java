package com.sc.demo.repository;

import com.sc.demo.model.announcements.AnnouncementsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsAttachmentRepo extends JpaRepository<AnnouncementsAttachment, Long> {
}
