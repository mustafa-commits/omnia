package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController implements SecuredRestController {

    @Autowired
    private NotificationService notificationService;

    // جلب اشعارات التلفون
    @GetMapping("/V1/api/sc/getPhoneNotification")
    public List<PhoneNotificationRequest> getPhoneNotification(@RequestHeader(name = "authorization") String token){
        return notificationService.phoneNotification(token);
    }
}
