package com.sc.demo.repository.notifications;

import com.sc.demo.model.users.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepo extends JpaRepository<Token, Long> {
}
