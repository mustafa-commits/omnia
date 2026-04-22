package com.sc.demo.controller;

import com.sc.demo.model.dto.*;
import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // انشاء اشعار
    @PostMapping("/V1/api/sc/createNotification")
    public NotificationMaster createNotification(@RequestBody NotificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }

    // تخزين Token
    @PostMapping("/V1/api/sc/setNotificationToken")
    public long setNotificationToken(@RequestBody NotificationTokenRequest notificationTokenRequest){
        return notificationService.saveToken(notificationTokenRequest);
    }

    // اشعارات التلفون
    @GetMapping("/V1/api/sc/getPHoneNotification")
    public PHoneNotificationRequest getPHoneNotification(@RequestParam long user_id){
        return notificationService.PHoneNotification(user_id);
    }

    // اشعارات الداشبورد حسب النوع الاشعار خاص او عام
    @GetMapping("/V1/api/sc/getNotificationByType")
    public NotificationByType getPNotificationByType(@RequestParam long notification_type){
        return notificationService.NotificationByType(notification_type);
    }

    // جميع اشعارات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllNotificationFamily")
    public AllNotificationFamilyRequest getAllNotificationFamily(@RequestParam long user_id){
        return notificationService.AllNotificationFamily(user_id);
    }

}
