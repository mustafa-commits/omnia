package com.sc.demo.repository.notifications;

import com.sc.demo.model.notification.notificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDetailsRepo extends JpaRepository<notificationDetails, Long> {
}
