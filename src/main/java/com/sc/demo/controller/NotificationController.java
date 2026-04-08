package com.sc.demo.controller;

import com.sc.demo.model.notification.Notification;
import com.sc.demo.model.users.dto.AllNotificationFamily;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.model.users.dto.NotificationByType;
import com.sc.demo.model.users.dto.PHoneNotification;
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

//    @GetMapping("/V1/api/sc/getNotification")
//    public Notification getNotification(@RequestParam long user_id){
//        return notificationService.notification1(user_id);
//    }


//    @GetMapping("/V2/api/sc/app/getNotification")
//    public NotificationResponse getNotification2(@RequestParam long user_id){
//        return notificationService.notification(user_id);
//    }

    // اشعارات التلفون
    @GetMapping("/V1/api/sc/getPHoneNotification")
    public PHoneNotification getPHoneNotification(@RequestParam long user_id){
        return notificationService.PHoneNotification(user_id);
    }

    // اشعارات الداشبورد حسب النوع الاشعار خاص او عام
    @GetMapping("/V1/api/sc/getNotificationByType")
    public NotificationByType getPNotificationByType(@RequestParam long notification_type){
        return notificationService.NotificationByType(notification_type);
    }

    // جميع اشعارات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllNotificationFamily")
    public AllNotificationFamily getAllNotificationFamily(@RequestParam long user_id){
        return notificationService.AllNotificationFamily(user_id);
    }

}
