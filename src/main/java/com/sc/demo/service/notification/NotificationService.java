package com.sc.demo.service.notification;

import com.google.firebase.messaging.*;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.users.Token;
import com.sc.demo.model.notification.SendingType;
import com.sc.demo.repository.notifications.NotificationDetailsRepo;
import com.sc.demo.repository.notifications.NotificationRepo;
import com.sc.demo.repository.notifications.NotificationTokenRepo;
import com.sc.demo.service.token.TokenService;
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

    @Autowired
    private TokenService tokenService;

    // انشاء اشعار
    public NotificationMaster createNotification(NotificationRequest notificationRequest) {

        Map<String, String> map = new HashMap<>();
        map.put("notification_type", "3");
        map.put("content_available", "1");

        NotificationMaster notificationMaster = new NotificationMaster(notificationRequest.createBy(),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.sendingType());

        Notification firebaseNotification = Notification
                .builder()
                .setTitle(notificationRequest.title())
                .setBody(notificationRequest.description())
                .build();

        List<Message> messageList = List.of();
        ApnsConfig apnsConfig = getApnsConfig();

        notificationMaster = notificationRepo.save(notificationMaster);
        if (notificationRequest.sendingType().equals(SendingType.PRIVATE)) {
            for (NotificationDetails n : notificationRequest.notificationDetails()) {
                notificationDetailsRepo.save(new NotificationDetails(n.getUserId(), n.getCreateBy(), notificationMaster));

                Optional<Token> byToken = notificationTokenRepo.findById(n.getUserId());

                if (byToken.isPresent()) {
                    messageList.add(Message.builder()
                            .setToken(byToken.get().getToken())
                            .putAllData(map)
                            .setNotification(firebaseNotification)
                            .setApnsConfig(apnsConfig)
                            .build()
                    );
                    notificationDetailsRepo.save(new NotificationDetails(n.getUserId(), n.getCreateBy(), notificationMaster));
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
        Optional<Token> byToken = notificationTokenRepo.findById(notificationTokenRequest.userId());

        if (byToken.isPresent()) {
            Token token = byToken.get();
            token.setLastUpdate(LocalDateTime.now());
            token.setToken(notificationTokenRequest.token());
            return notificationTokenRepo.save(token).getUserId();
        } else {
            Token token = new Token();
            token.setToken(notificationTokenRequest.token());
            token.setUserId(notificationTokenRequest.userId());
            return notificationTokenRepo.save(token).getUserId();
        }
    }

    // اشعارات التطيق لكل يوزر
    public List<PhoneNotificationRequest> phoneNotification(String token) {
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();

        return jdbcClient.sql("""
                   SELECT N.CREATE_DATE AS createDate, N.TITLE, N.DESCRIPTION
                   FROM SC_NOTIFICATION N
                   LEFT JOIN SC_NOTIFICATION_DETAILS ND ON N.NOTIFICATION_ID = ND.NOTIFICATION_ID
                   WHERE ND.USER_ID = :user_id
                   OR N.SENDING_TYPE = 0
                """)
                .param("user_id", userTokenId)
                .query(PhoneNotificationRequest.class)
                .list();
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
                   where SENDING_TYPE = :notification_type
                """)
                .param("notification_type",notification_type)
                .query(NotificationByType.class)
                .list();
    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllNotificationFamilyRequest> AllNotificationFamily() {

        return jdbcClient.sql("""
                   SELECT N.CREATE_DATE AS createDate, N.TITLE, N.DESCRIPTION, N.SENDING_TYPE AS sendingType
                   FROM SC_NOTIFICATION N
                   LEFT JOIN SC_NOTIFICATION_DETAILS ND ON N.NOTIFICATION_ID = ND.NOTIFICATION_ID
                """).query(AllNotificationFamilyRequest.class).list();
    }

    // حذف اشعار
    public Boolean deleteNotification(Long notificationId){
        if (!notificationRepo.findById(notificationId).equals(Optional.empty())){
            notificationRepo.deleteById(notificationId);
            return true;
        }
        return false;
    }


    private ApnsConfig getApnsConfig() {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("content_available",1);
        ApsAlert apsAlert= ApsAlert.builder().setTitle("AL-AYN").setSubtitle("اشعار").build();
        return ApnsConfig.builder()
                .setAps(Aps.builder().setSound("1").putAllCustomData(map2).setAlert(apsAlert).build()).build();
    }

}
