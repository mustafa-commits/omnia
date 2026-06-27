package com.sc.demo.service.notification;

import com.google.firebase.messaging.*;
import com.sc.demo.model.Tokens.AppToken;
import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.dto.notification.*;
import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.model.notification.NotificationMaster;
import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.notification.SendingType;
import com.sc.demo.repository.chat.TokenRepo;
import com.sc.demo.repository.notifications.NotificationDetailsRepo;
import com.sc.demo.repository.notifications.NotificationRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

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
    private TokenRepo tokenRepo;

    @Autowired
    private TokenService tokenService;

    // انشاء اشعار
    public NotificationMaster createNotification(NotificationRequest notificationRequest, String token) {
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        Map<String, String> map = new HashMap<>();
        map.put("notification_type", "3");
        map.put("content_available", "1");

        NotificationMaster notificationMaster = new NotificationMaster(Long.parseLong(userDashboardId),
                notificationRequest.title(), notificationRequest.description(),
                notificationRequest.sendingType());

        Notification firebaseNotification = Notification
                .builder()
                .setTitle(notificationRequest.title())
                .setBody(notificationRequest.description())
                .build();
        System.out.println("firebaseNotification " + firebaseNotification);

        List<Message> messageList = new ArrayList<>();
        ApnsConfig apnsConfig = getApnsConfig();
        System.out.println("messageList 1 " + messageList);
        System.out.println("apnsConfig " + apnsConfig);

        notificationMaster = notificationRepo.save(notificationMaster);
        if (notificationRequest.sendingType().equals(SendingType.PRIVATE)) {
            for (NotificationDetails n : notificationRequest.notificationDetails()) {
                notificationDetailsRepo.save(new NotificationDetails(n.getUserId(), Long.parseLong(userDashboardId), notificationMaster));

                Optional<AppToken> byToken = tokenRepo.findById(n.getUserId());
                System.out.println("byToken" + byToken);

                if (byToken.isPresent()) {
                    messageList.add(Message.builder()
                            .setToken(byToken.get().getToken())
                            .putAllData(map)
                            .setNotification(firebaseNotification)
                            .setAndroidConfig(AndroidConfig.builder()
                                            .setNotification(AndroidNotification.builder()
                                                    .setChannelId("ayn Family")
                                                    .build())
                                            .build())
                            .setApnsConfig(apnsConfig)
                            .build()
                    );
//                    notificationDetailsRepo.save(new NotificationDetails(n.getUserId(), Long.parseLong(userDashboardId), notificationMaster));
                    System.out.println("messageList 2 " + messageList);
                }
            }
            if (messageList.size() >= 1) {
                try {
                    System.out.println(firebaseMessaging.send(messageList.get(0)).toString());
                } catch (FirebaseMessagingException e) {
                    throw new RuntimeException(e);
                }
            }
            return notificationMaster;
        }

        Message message = Message.builder()
                .setTopic("all")
                .putAllData(map)
                .setNotification(firebaseNotification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setNotification(AndroidNotification.builder()
                                .setChannelId("ayn Family")
                                .build())
                        .build())
                .setApnsConfig(apnsConfig)
                .build();
        firebaseMessaging.sendAsync(message);
        System.out.println("message" + message);
        return notificationMaster;
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(TokenRequest tokenRequest) {
        Optional<AppToken> byToken = tokenRepo.findById(tokenRequest.userId());

        if (byToken.isPresent()) {
            AppToken appToken = byToken.get();
            appToken.setLastUpdate(LocalDateTime.now());
            appToken.setToken(tokenRequest.token());
            appToken.setTokenType(Platform.APP);
            return tokenRepo.save(appToken).getUserId();
        } else {
            AppToken appToken = new AppToken();
            appToken.setToken(tokenRequest.token());
            appToken.setUserId(tokenRequest.userId());
            appToken.setTokenType(Platform.APP);
            return tokenRepo.save(appToken).getUserId();
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
                   ORDER BY N.CREATE_DATE DESC
                """)
                .param("user_id", userTokenId)
                .query(PhoneNotificationRequest.class)
                .list();
    }

    // الداش بورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllNotificationFamilyRequest> AllNotificationFamily() {

        return jdbcClient.sql("""
                SELECT N.NOTIFICATION_ID AS notificationId
                       ,N.CREATE_DATE AS createDate
                       ,N.TITLE
                       ,N.DESCRIPTION
                       ,N.SENDING_TYPE AS sendingType
                FROM SC_NOTIFICATION N
                LEFT JOIN SC_NOTIFICATION_DETAILS ND ON N.NOTIFICATION_ID = ND.NOTIFICATION_ID
                ORDER BY N.CREATE_DATE DESC
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

    // تعديل اشعار
    public Boolean editNotification(Long notificationId, String title, String description, String token){

        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        NotificationMaster updateNotification = notificationRepo.findById(notificationId).get();
        if (title != null) {
            updateNotification.setTitle(title);
        }
        if (description != null) {
            updateNotification.setDescription(description);
        }
        updateNotification.setLastUpdateBy(Long.parseLong(userDashboardId));
        updateNotification.setLastUpdate(LocalDateTime.now());

        notificationRepo.save(updateNotification);
        return true;
    }

    private ApnsConfig getApnsConfig() {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("content_available",1);
        ApsAlert apsAlert= ApsAlert.builder().setTitle("AL-AYN Family").setSubtitle("اشعار").build();
        return ApnsConfig.builder()
                .setAps(Aps.builder().setSound("1").putAllCustomData(map2).setAlert(apsAlert).build()).build();
    }

}
