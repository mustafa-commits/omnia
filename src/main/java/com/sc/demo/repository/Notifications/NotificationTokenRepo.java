package com.sc.demo.repository.Notifications;

import com.sc.demo.model.notification.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepo extends JpaRepository<NotificationToken, Long> {
}
