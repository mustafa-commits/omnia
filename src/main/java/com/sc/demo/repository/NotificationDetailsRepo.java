package com.sc.demo.repository;

import com.sc.demo.model.notification.NotificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDetailsRepo extends JpaRepository<NotificationDetails, Long> {
}
