package com.sc.demo.service.notification;

import com.sc.demo.model.notification.Notification;
import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.model.users.dto.NotificationResponse;
import com.sc.demo.repository.AppUserRepo;
import com.sc.demo.repository.NotificationDetailsRepo;
import com.sc.demo.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private JdbcClient jdbcClient;


    @Autowired
    private NotificationDetailsRepo notificationDetailsRepo;

    public Notification notification1(long notification_id) {
        Optional<Notification> byId = notificationRepo.findById(notification_id);

        if (byId.isPresent()) {
            return byId.get();
        } else
            return null;

    }

    public Notification createNotification(NotificationRequest notificationRequest){

        Notification notification = new Notification(notificationRequest.sendId(),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.notificationDetails()
        );

        notification= notificationRepo.save(notification);
        return notification;
    }

    public NotificationResponse notification(long user_id) {

        NotificationResponse NoteCome = jdbcClient.sql("""
                    select N.TITLE, N.DESCRIPTION, N.send_id
                    from SC_NOTIFICATION N
                            left join SC_NOTIFICATION_DETAILS ND on N.NOTIFICATION_ID = ND.NOTIFICATION_ID where user_id = :user_id
                """).param("user_id",user_id).query(NotificationResponse.class).single();

        return NoteCome;
    }

}
