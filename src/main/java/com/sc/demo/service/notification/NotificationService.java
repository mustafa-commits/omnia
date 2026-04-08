package com.sc.demo.service.notification;

import com.sc.demo.model.notification.Notification;
import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.dto.AllNotificationFamily;
import com.sc.demo.model.dto.NotificationRequest;
import com.sc.demo.model.dto.NotificationByType;
import com.sc.demo.model.dto.PHoneNotification;
import com.sc.demo.repository.NotificationDetailsRepo;
import com.sc.demo.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private NotificationDetailsRepo notificationDetailsRepo;

    // انشاء اشعار
    public Notification createNotification(NotificationRequest notificationRequest){
        Notification notification = new Notification(notificationRequest.sendId(),
                notificationRequest.title(), notificationRequest.description(),
                 notificationRequest.notificationType()
        );
        notification= notificationRepo.save(notification);
        for (NotificationDetails n :notificationRequest.notificationDetails()){
            notificationDetailsRepo.save(new NotificationDetails(n.getUser_id(), notification));
        }
        return notification;
    }

    // اشعارات التطيق لكل يززر
    public PHoneNotification PHoneNotification(long user_id) {
        Optional <PHoneNotification>  pNote = jdbcClient.sql("""
                   select n.CREATE_DATE as createDate, n.TITLE, n.DESCRIPTION
                   from SC_NOTIFICATION n
                   join SC_NOTIFICATION_DETAILS nd on n.NOTIFICATION_ID = nd.NOTIFICATION_ID
                   Where ND.user_id = :user_id
                """).param("user_id",user_id).query(PHoneNotification.class).optional();

        if (pNote.isPresent())
            return pNote.get();
            else
                return null;
    }

    // جلب اشعارات الداشبورد حسب النوع (عامة او خاصة)
    public NotificationByType NotificationByType(long notification_type) {
        Optional <NotificationByType> dNote = jdbcClient.sql("""
                   SELECT SEND_ID as sendId, TITLE, DESCRIPTION, CREATE_DATE as createDate
                   FROM SC_NOTIFICATION
                   where notification_type = :notification_type;
                """).param("notification_type",notification_type).query(NotificationByType.class).optional();

        if (dNote.isPresent())
            return dNote.get();
        else
            return null;
    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public AllNotificationFamily AllNotificationFamily(long user_id) {

        Optional <AllNotificationFamily>  allDNote = jdbcClient.sql("""
                   select n.CREATE_DATE as createDate, n.TITLE, n.DESCRIPTION, n.notification_type as NotificationType
                   from SC_NOTIFICATION n
                   join SC_NOTIFICATION_DETAILS nd on n.NOTIFICATION_ID = nd.NOTIFICATION_ID
                   Where ND.user_id = :user_id
                """).param("user_id",user_id).query(AllNotificationFamily.class).optional();

        if (allDNote.isPresent())
            return allDNote.get();
        else
            return null;
    }
}
