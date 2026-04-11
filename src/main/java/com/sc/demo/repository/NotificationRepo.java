package com.sc.demo.repository;

import com.sc.demo.model.notification.NotificationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationMaster, Long> {
}
