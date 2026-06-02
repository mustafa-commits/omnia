package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.model.notification.notificationMaster;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class notificationController implements SecuredRestController {

    @Autowired
    private NotificationService notificationService;

    // انشاء اشعار
    @PostMapping("/V1/api/sc/createNotification")
    public notificationMaster createNotification(@RequestBody notificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }

    // تخزين Token
    @PostMapping("/V1/api/sc/setNotificationToken")
    public long setNotificationToken(@RequestBody notificationTokenRequest notificationTokenRequest){
        return notificationService.saveToken(notificationTokenRequest);
    }

    // جلب اشعارات التلفون
    @GetMapping("/V1/api/sc/getPHoneNotification")
    public List<phoneNotificationRequest> getPHoneNotification(@RequestHeader(name = "authorization") String token){
        return notificationService.phoneNotification(token);
    }

    // جلب اشعارات الداشبورد حسب النوع الاشعار خاص او عام
    @GetMapping("/V1/api/sc/getNotificationByType")
    public List<notificationByType> getPNotificationByType(@RequestParam long notification_type){
        return notificationService.NotificationByType(notification_type);
    }

    // جلب جميع اشعارات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllNotificationFamily")
    public List<allNotificationFamilyRequest> getAllNotificationFamily(){
        return notificationService.AllNotificationFamily();
    }

}
