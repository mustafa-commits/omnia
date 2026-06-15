package com.sc.demo.repository.announcements;

import com.sc.demo.model.users.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsTokenRepo extends JpaRepository<Token, Long> {
}
