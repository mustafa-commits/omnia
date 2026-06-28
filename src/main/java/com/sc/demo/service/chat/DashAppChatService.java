package com.sc.demo.service.chat;

import com.google.firebase.messaging.*;
import com.sc.demo.model.tokens.AppToken;
import com.sc.demo.model.chat.*;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.repository.chat.ChatRepo;
import com.sc.demo.repository.chat.AppChatDetailsRepo;
import com.sc.demo.repository.chat.TokenRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashAppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Environment environment;

    @Autowired
    private AppChatDetailsRepo appChatDetailsRepo;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private TokenRepo tokenRepo;

    // ارسال رسالة في الداش بورد
    public boolean dashWriteMessages(MessagesRequest messagesRequest, MultipartFile file, MultipartFile voice, String token) {
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();
        String newFileName = null;

        Map<String, String> map = new HashMap<>();
        map.put("SENDING_TYPE_NOTIFICATION", "2");
        map.put("content_available", "1");

        if(messagesRequest.msgType() == MsgType.IMAGE) {
            try {
                String originalFileName = file.getOriginalFilename();
                newFileName = System.nanoTime() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = environment.getProperty("ATTACHMENT_PATH_CHAT") + newFileName;
                file.transferTo(new File(filePath));
                System.out.println(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(messagesRequest.msgType() == MsgType.VOICE) {
            try {
                String originalVoiceName = voice.getOriginalFilename();
                newFileName = System.nanoTime() + originalVoiceName.substring(originalVoiceName.lastIndexOf("."));
                String filePath = environment.getProperty("ATTACHMENT_PATH_VOICE") + newFileName;
                voice.transferTo(new File(filePath));
                System.out.println(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        WhoIsSender whoIsSender = Platform.APP.equals(messagesRequest.platform())
                ? WhoIsSender.APPUSER
                : WhoIsSender.USERDASHBOARD;

        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                Long.parseLong(userDashboardId), whoIsSender, messagesRequest.platform(),
                messagesRequest.messages().isEmpty() ? newFileName : messagesRequest.messages(),
                messagesRequest.msgType());

        Notification firebaseNotification = Notification
                .builder()
                .setTitle("أحبة العين")
                .setBody(messagesRequest.messages())
                .build();

        List<Message> messageList = new ArrayList<>();
        ApnsConfig apnsConfig = getApnsConfig();

        appChatDetailsRepo.save(appChatDetails).getDetailsChatId();

        Optional<AppToken> byToken = tokenRepo.findById(Long.parseLong(userDashboardId));

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

        if (messageList.size() >= 1) {
            try {
                System.out.println(firebaseMessaging.send(messageList.get(0)).toString());
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    // جلب الرسائل
    public List<MessagesResponse> getDashMessages(Long chatId){
        jdbcClient.sql("""
                UPDATE MOBAPP.SC_CHAT_DETAILS
                SET SEEN_AT = SYSDATE
                WHERE CHAT_ID = :chatId
                AND PLATFORM = 0
                AND SEEN_AT IS NULL
                """)
                .param("chatId",chatId)
                .update();
        
        return jdbcClient.sql("""
                SELECT CASE WHEN MSG_TYPE in (1,2) THEN TO_CHAR(:path) || MESSAGES
                        ELSE MESSAGES END AS messages,
                        WHO_IS_SENDER AS whoIsSender,
                        CREATE_BY AS createBy,
                        CREATE_DATE AS createDate,
                        PLATFORM AS platForm,
                        SEEN_AT AS seenAt,
                        MSG_ACTIVITY AS msgActivity
                FROM MOBAPP.SC_CHAT_DETAILS
                WHERE CHAT_ID = :chatId
                ORDER BY CREATE_DATE DESC
                """)
                .param("chatId", chatId)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/photoChat/")
                .query(MessagesResponse.class)
                .list();
    }

    // جلب المحادثات المفعلة
    public List<DashAppChatResponse> dashPhoneChats(){
        return jdbcClient.sql("""
            SELECT M.CHAT_ID AS chatId
                  ,U.GUARDIAN_NAME AS guardianName
                  ,M.CHAT_TITLE AS chatTitle
                  ,D.MESSAGES AS messages
                  ,D.CREATE_DATE AS createDate
                  ,M.MSG_ACTIVE AS msgActive
            FROM MOBAPP.SC_CHAT_MASTER M
            LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
            LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
            WHERE M.MSG_ACTIVE = 0
            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            ORDER BY M.CREATE_DATE DESC
            """)
            .query(DashAppChatResponse.class)
            .list();
    }

    // جلب المحادثات المؤرشفة
    public List<DashAppChatResponse> dashPhoneChatsArchived(){
        return jdbcClient.sql("""
            SELECT M.CHAT_ID AS chatId
                  ,U.GUARDIAN_NAME AS guardianName
                  ,M.CHAT_TITLE AS chatTitle
                  ,D.MESSAGES AS messages
                  ,D.CREATE_DATE AS createDate
                  ,M.MSG_ACTIVE AS msgActive
            FROM MOBAPP.SC_CHAT_MASTER M
            LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
            LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
            WHERE M.MSG_ACTIVE IN (1, 2)
            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            ORDER BY M.CREATE_DATE DESC
            """)
                .query(DashAppChatResponse.class)
                .list();
    }

    // طلب ارشفة المحادثات
    public Boolean requestArchivedChat(Long chatId, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<AppChatMaster> byChatId = chatRepo.findById(chatId);
        System.out.println(byChatId);

        if (byChatId.isPresent()) {
            if (byChatId.get().getMsgActive().equals(MsgActive.ACTIVE)) {
                AppChatDetails closeMessage = new AppChatDetails();
                closeMessage.setMessages("""
                        نود اعلامكم سيتم انهاء المحادثة تلقائيآ في غضون(12 ساعة) في حال لديكم استفسار اخرى يرجى الضغط على كلمة(استمرار) وفي حال عدم وجود استفسار الضغظ على كلمة(اغلاق)
                        """);
                closeMessage.setCreateDate(LocalDateTime.now());
                closeMessage.setCreateBy(Long.parseLong(userDashboardId));
                closeMessage.setPlatform(Platform.SYSTEM);
                closeMessage.setMsgActivity(MsgActivity.CLOSE_REQUEST);
                closeMessage.setWhoIsSender(WhoIsSender.SYSTEM);
                closeMessage.setChatApp(byChatId.get());
                appChatDetailsRepo.save(closeMessage);
                byChatId.get().setMsgActive(MsgActive.PENDING);
                byChatId.get().setLastUpdate(LocalDateTime.now());
                byChatId.get().setLastUpdateBy(Long.parseLong(userDashboardId));
                chatRepo.save(byChatId.get());
            }
        }
        return true;
    }

    // الموافقة على اغلاق المحادثة او لا
    public Boolean askArchivedChat(Long chatId, ConfirmProcedure confirmProcedure, String token){
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<AppChatMaster> byChatId = chatRepo.findById(chatId);

        if (confirmProcedure.equals(ConfirmProcedure.YES)) {
            AppChatDetails closeChat = new AppChatDetails();
            closeChat.setMessages("""
                        تم اغلاق المحادثة
                        """);
            closeChat.setCreateDate(LocalDateTime.now());
            closeChat.setCreateBy(Long.parseLong(userTokenId));
            closeChat.setPlatform(Platform.SYSTEM);
            closeChat.setMsgActivity(MsgActivity.NOT_ACTIVE);
            closeChat.setConfirmProcedure(ConfirmProcedure.YES);
            closeChat.setChatApp(byChatId.get());
            appChatDetailsRepo.save(closeChat);
            byChatId.get().setMsgActive(MsgActive.ARCHIVED);
            byChatId.get().setLastUpdate(LocalDateTime.now());
            byChatId.get().setLastUpdateBy(Long.parseLong(userTokenId));
            chatRepo.save(byChatId.get());
        }else {
            AppChatDetails openChat = new AppChatDetails();
            openChat.setMessages("""
                        تفضلوا بطرح استفساركم
                        """);
            openChat.setCreateDate(LocalDateTime.now());
            openChat.setCreateBy(Long.parseLong(userTokenId));
            openChat.setPlatform(Platform.SYSTEM);
            openChat.setMsgActivity(MsgActivity.ACTIVE);
            openChat.setConfirmProcedure(ConfirmProcedure.NO);
            openChat.setChatApp(byChatId.get());
            appChatDetailsRepo.save(openChat);
            byChatId.get().setMsgActive(MsgActive.ACTIVE);
            byChatId.get().setLastUpdate(LocalDateTime.now());
            byChatId.get().setLastUpdateBy(Long.parseLong(userTokenId));
            chatRepo.save(byChatId.get());
        }
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
