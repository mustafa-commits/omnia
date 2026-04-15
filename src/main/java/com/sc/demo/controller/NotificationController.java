package com.sc.demo.controller;

import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.model.dto.AllNotificationFamilyRequest;
import com.sc.demo.model.dto.NotificationRequest;
import com.sc.demo.model.dto.NotificationByType;
import com.sc.demo.model.dto.PHoneNotificationRequest;
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
