package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.dto.notification.AllNotificationFamilyRequest;
import com.sc.demo.model.dto.notification.NotificationByType;
import com.sc.demo.model.dto.notification.NotificationRequest;
import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DashNotificationController implements SecuredRestController {

    @Autowired
    private NotificationService notificationService;

    // انشاء اشعار
    @PostMapping("/V1/api/sc/createNotification")
    public NotificationMaster createNotification(@RequestBody NotificationRequest notificationRequest){
        return notificationService.createNotification(notificationRequest);
    }

    // جلب اشعارات الداشبورد حسب النوع الاشعار خاص او عام
    @GetMapping("/V1/api/sc/getNotificationByType")
    public List<NotificationByType> getPNotificationByType(@RequestParam long notification_type){
        return notificationService.NotificationByType(notification_type);
    }

    // جلب جميع اشعارات الداشبورد للعائلة
    @GetMapping("/V1/api/sc/getAllNotificationFamily")
    public List<AllNotificationFamilyRequest> getAllNotificationFamily(){
        return notificationService.AllNotificationFamily();
    }

    // حذف اشعار
    @DeleteMapping("/V1/api/sc/deleteNotification")
    public Boolean deleteNotification(@RequestParam Long notificationId){
        return notificationService.deleteNotification(notificationId);
    }
    // تعديل اشعار
    @PutMapping("/V1/api/sc/editNotification")
    public Boolean editNotification(@RequestParam Long notificationId,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) String description,
                                               @RequestHeader(name = "authorization") String token){
        return notificationService.editNotification(notificationId, title, description, token);
    }
}
