package com.sc.demo.service.users;

import com.sc.demo.model.users.Notification;
import com.sc.demo.model.users.dto.NotificationRequest;
import com.sc.demo.model.users.dto.NotificationResponse;
import com.sc.demo.repository.AppUserRepo;
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
    private AppUserRepo appUserRepo;

    @Autowired
    private JdbcClient jdbcClient;

    public Notification notification1(long notification_id) {
        Optional<Notification> byId = appUserRepo.findById(notification_id);

        if (byId.isPresent()) {
            return byId.get();
        } else
            return null;

    }

    public Notification createNotification(NotificationRequest notificationRequest){
        Notification entity = new Notification(notificationRequest.sendId(), notificationRequest.receiveId(),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.notificationDetails());

        Notification notification = notificationRepo.save(entity);

        return notification;
    }

    public NotificationResponse notification(long id) {

        NotificationResponse NoteCome = jdbcClient.sql("""
                    select N.TITLE, N.DESCRIPTION, ND.USER_ID from SC_NOTIFICATION N
                            left join SC_NOTIFICATION_DETAILS ND on N.NOTIFICATION_ID = ND.NOTIFICATION_ID where id = :Id
                """).param("Id",id).query(NotificationResponse.class).single();

        return NoteCome;
    }

}
