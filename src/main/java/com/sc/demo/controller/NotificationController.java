package com.sc.demo.controller;

import com.sc.demo.model.notification.Notification;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/V1/api/sc/createNotification")
    public Notification createNotification(@RequestBody NotificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }

    @GetMapping("/V1/api/sc/getNotification")
    public Notification getNotification(@RequestParam long nid){
        return notificationService.notification1(nid);
    }
}
