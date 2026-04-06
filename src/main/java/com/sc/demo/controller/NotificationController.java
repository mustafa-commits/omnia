package com.sc.demo.controller;

import com.sc.demo.model.notification.Notification;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.model.users.dto.NotificationResponse;
import com.sc.demo.model.users.dto.PublicNotification;
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
    public Notification getNotification(@RequestParam long user_id){
        return notificationService.notification1(user_id);
    }


    @GetMapping("/V2/api/sc/getNotification")
    public NotificationResponse getNotification2(@RequestParam long user_id){
        return notificationService.notification(user_id);
    }

    @GetMapping("/V1/api/sc/getPublicNotification")
    public PublicNotification getPublicNotification(@RequestParam long user_id){
        return notificationService.PublicNotification(user_id);
    }

//    @GetMapping("/V1/api/sc/getPrivateNotification")
//    public NotificationResponse getNotification2(@RequestParam long user_id){
//        return notificationService.notification(user_id);
//    }

}
