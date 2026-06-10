package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.model.notification.NotificationMaster;
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
    public NotificationMaster createNotification(@RequestBody NotificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }

    // تخزين Token
    @PostMapping("/V1/api/sc/setNotificationToken")
    public long setNotificationToken(@RequestBody NotificationTokenRequest notificationTokenRequest){
        return notificationService.saveToken(notificationTokenRequest);
    }

    // جلب اشعارات التلفون
    @GetMapping("/V1/api/sc/getPHoneNotification")
    public List<PhoneNotificationRequest> getPHoneNotification(@RequestHeader(name = "authorization") String token){
        return notificationService.phoneNotification(token);
    }



}
