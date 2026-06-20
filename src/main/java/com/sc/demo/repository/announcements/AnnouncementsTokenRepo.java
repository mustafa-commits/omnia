package com.sc.demo.repository.announcements;

import com.sc.demo.model.Tokens.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsTokenRepo extends JpaRepository<AppToken, Long> {
}
