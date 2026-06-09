package com.sc.demo.repository.announcements;

import com.sc.demo.model.announcements.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsRepo extends JpaRepository<Announcements, Long> {
}
