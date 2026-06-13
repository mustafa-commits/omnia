package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController implements SecuredRestController {

    @Autowired
    private NotificationService notificationService;

    // تخزين Token
    @PostMapping("/V1/api/sc/setNotificationToken")
    public long setNotificationToken(@RequestBody NotificationTokenRequest notificationTokenRequest){
        return notificationService.saveToken(notificationTokenRequest);
    }

    // جلب اشعارات التلفون
    @GetMapping("/V1/api/sc/getPhoneNotification")
    public List<PhoneNotificationRequest> getPhoneNotification(@RequestHeader(name = "authorization") String token){
        return notificationService.phoneNotification(token);
    }



}
