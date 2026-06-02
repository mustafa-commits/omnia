package com.sc.demo.repository.notifications;

import com.sc.demo.model.notification.notificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepo extends JpaRepository<notificationToken, Long> {
}
