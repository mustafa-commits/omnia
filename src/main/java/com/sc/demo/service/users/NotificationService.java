package com.sc.demo.service.users;

import com.sc.demo.model.users.Notification;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public Notification createNotification(NotificationRequest notificationRequest){
        Notification entity = new Notification(notificationRequest.sendId(), notificationRequest.receiveId(),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.notificationDetails());

        Notification notification = notificationRepo.save(entity);

        return notification;
    }
}
