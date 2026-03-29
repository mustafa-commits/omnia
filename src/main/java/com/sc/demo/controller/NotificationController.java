package com.sc.demo.controller;

import com.sc.demo.model.users.Notification;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.repository.NotificationRepo;
import com.sc.demo.service.users.LoginService;
import com.sc.demo.service.users.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/V1/api/sc/createNotification")
    public Notification createNotification(@RequestBody NotificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }
}
