package com.sc.demo.repository.announcements;

import com.sc.demo.model.announcements.AnnouncementsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementsDetailsRepo extends JpaRepository<AnnouncementsDetails, Long> {
}
