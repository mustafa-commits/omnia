package com.sc.demo.repository.notifications;

import com.sc.demo.model.notification.notificationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<notificationMaster, Long> {
}
