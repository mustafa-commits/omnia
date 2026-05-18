package com.sc.demo.service.notification;

import com.google.firebase.messaging.*;
import com.sc.demo.model.dto.Notification.*;
import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.notification.NotificationToken;
import com.sc.demo.model.notification.NotificationType;
import com.sc.demo.repository.NotificationDetailsRepo;
import com.sc.demo.repository.NotificationRepo;
import com.sc.demo.repository.NotificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private NotificationDetailsRepo notificationDetailsRepo;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private NotificationTokenRepo notificationTokenRepo;

    // انشاء اشعار
    public NotificationMaster createNotification(NotificationRequest notificationRequest) {

        Map<String, String> map = new HashMap<>();
        map.put("notification_type", "3");
        map.put("content_available", "1");

        NotificationMaster notificationMaster = new NotificationMaster(notificationRequest.sendId(),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.notificationType()
        );

        Notification firebaseNotification = Notification
                .builder()
                .setTitle(notificationRequest.title())
                .setBody(notificationRequest.description())
                .build();

        List<Message> messageList = List.of();
        ApnsConfig apnsConfig = getApnsConfig();

        notificationMaster = notificationRepo.save(notificationMaster);
        if (notificationRequest.notificationType().equals(NotificationType.PRIVATE)) {
            for (NotificationDetails n : notificationRequest.notificationDetails()) {
                notificationDetailsRepo.save(new NotificationDetails(n.getUser_id(), notificationMaster));

                Optional<NotificationToken> byToken = notificationTokenRepo.findById(n.getUser_id());

                if (byToken.isPresent()) {
                    messageList.add(Message.builder()
                            .setToken(byToken.get().getToken())
                            .putAllData(map)
                            .setNotification(firebaseNotification)
                            .setApnsConfig(apnsConfig)
                            .build()
                    );
                    notificationDetailsRepo.save(new NotificationDetails(n.getUser_id(), notificationMaster));
                }
            }
            if (messageList.size() >= 1) {
                firebaseMessaging.sendEachAsync(messageList);
            }
            return notificationMaster;
        }

        Message message = Message.builder()
                .setTopic("all")
                .putAllData(map)
                .setNotification(firebaseNotification)
                .setApnsConfig(apnsConfig)
                .build();
        firebaseMessaging.sendAsync(message);

        return notificationMaster;
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(NotificationTokenRequest notificationTokenRequest) {
        Optional<NotificationToken> byToken = notificationTokenRepo.findById(notificationTokenRequest.userId());

        if (byToken.isPresent()){
            NotificationToken notificationToken = byToken.get();
            notificationToken.setLastUpdate(LocalDateTime.now());
            notificationToken.setToken(notificationTokenRequest.Token());
            return notificationTokenRepo.save(notificationToken).getUser_id();
        } else {
            NotificationToken notificationToken = byToken.get();
            notificationToken.setToken(notificationTokenRequest.Token());
            notificationToken.setUser_id(notificationTokenRequest.userId());
            return notificationTokenRepo.save(notificationToken).getUser_id();
        }
    }


    // اشعارات التطيق لكل يوزر
    public List<PHoneNotificationRequest> PHoneNotification(long user_id) {
        return jdbcClient.sql("""
                   SELECT N.CREATE_DATE AS createDate, N.TITLE, N.DESCRIPTION
                   FROM SC_NOTIFICATION N
                   LEFT JOIN SC_NOTIFICATION_DETAILS ND ON N.NOTIFICATION_ID = ND.NOTIFICATION_ID
                   WHERE ND.USER_ID = :user_id
                   OR N.NOTIFICATION_TYPE = 0
                """).param("user_id",user_id).query(PHoneNotificationRequest.class).list();
    }

    // جلب اشعارات الداشبورد حسب النوع (عامة او خاصة)
    public List<NotificationByType> NotificationByType(long notification_type) {
        return jdbcClient.sql("""
                   SELECT SEND_ID as sendId
                          ,TITLE
                          ,DESCRIPTION
                          ,CREATE_DATE as createDate
                          ,(SELECT LISTAGG(USER_ID, ', ') WITHIN GROUP (ORDER BY NOTIFICATION_ID) as UserListing
                   FROM MOBAPP.SC_NOTIFICATION_DETAILS ND
                   WHERE ND.NOTIFICATION_ID = N.NOTIFICATION_ID) AS USER_ID
                   FROM SC_NOTIFICATION N
                   where notification_type = :notification_type
                """)
                .param("notification_type",notification_type)
                .query(NotificationByType.class)
                .list();
    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllNotificationFamilyRequest> AllNotificationFamily() {

        return jdbcClient.sql("""
                   SELECT N.CREATE_DATE AS createDate, N.TITLE, N.DESCRIPTION, N.NOTIFICATION_TYPE AS NotificationType
                   FROM SC_NOTIFICATION N
                   LEFT JOIN SC_NOTIFICATION_DETAILS ND ON N.NOTIFICATION_ID = ND.NOTIFICATION_ID
                """).query(AllNotificationFamilyRequest.class).list();
    }


    private ApnsConfig getApnsConfig() {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("content_available",1);
        ApsAlert apsAlert= ApsAlert.builder().setTitle("AL-AYN").setSubtitle("اشعار").build();
        return ApnsConfig.builder()
                .setAps(Aps.builder().setSound("1").putAllCustomData(map2).setAlert(apsAlert).build()).build();
    }
}
