package com.sc.demo.service.chat;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.chat.*;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.repository.chat.ChatRepo;
import com.sc.demo.repository.chat.MessagesRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private MessagesRepo messagesRepo;

    // ارسال رسالة في الداش بورد
    public boolean dashWriteMessages(MessagesRequest messagesRequest, MultipartFile file, MultipartFile voice, String token) {
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();
        String newFileName = null;

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
                ? WhoIsSender.USER
                : WhoIsSender.EMPLOYEE;

        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                Long.parseLong(employeesId), whoIsSender, messagesRequest.platform(),
                messagesRequest.messages().isEmpty() ? newFileName : messagesRequest.messages(),
                messagesRequest.msgType());
        Long detailsChatId = messagesRepo.save(appChatDetails).getDetailsChatId();
        System.out.println(detailsChatId);
        return true;
    }

    // جلب الرسائل
    public List<MessagesResponse> getDashMessages(Long chatId){
        System.out.println(chatId);
        return jdbcClient.sql("""
                SELECT CASE WHEN MSG_TYPE in (1,2) THEN TO_CHAR(:path) || MESSAGES
                        ELSE MESSAGES END AS messages,
                        WHO_IS_SENDER AS whoIsSender,
                        CREATE_BY AS createBy,
                        CREATE_DATE AS createDate,
                        PLATFORM AS platForm
                FROM MOBAPP.SC_CHAT_DETAILS
                WHERE CHAT_ID = :chatId
                order by CREATE_DATE desc
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
             FROM MOBAPP.SC_CHAT_MASTER M
             LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
             LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
             WHERE D.MSG_ACTIVITY = 0
             AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
             ORDER BY M.CHAT_ID DESC
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
            FROM MOBAPP.SC_CHAT_MASTER M
            LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
            LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
            WHERE D.MSG_ACTIVITY = 1
            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            ORDER BY M.CHAT_ID DESC
            """)
                .query(DashAppChatResponse.class)
                .list();
    }

    // select chatMaster active = pending
    // get chatDetails with status CLOSE_REQUEST and check createDate is after  now
    // change chatMaster status to archive

    // طلب ارشفة المحادثات
    public Boolean requestArchivedChat(Long chatId, String token){
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<AppChatMaster> byChatId = chatRepo.findById(chatId);
        System.out.println(byChatId);

        if (byChatId.isPresent()) {
            if (byChatId.get().getMsgActive().equals(MsgActive.ACTIVE)) {
                AppChatDetails closeMessage = new AppChatDetails();
                closeMessage.setMessages("""
                        نود اعلامكم سيتم انهاء المحادثة تلقائيآ في غضون(12 ساعة)
                        في حال لديكم استفسار اخرى يرجى الضغط على كلمة(نعم)
                        وفي حال عدم وجود استفسار الضغظ على كلمة(اغلاق)
                        """);
                closeMessage.setCreateDate(LocalDateTime.now());
                closeMessage.setCreateBy(Long.parseLong(employeesId));
                closeMessage.setPlatform(Platform.SYSTEM);
                closeMessage.setMsgActivity(MsgActivity.CLOSE_REQUEST);
                closeMessage.setChatApp(byChatId.get());
                messagesRepo.save(closeMessage);
                byChatId.get().setMsgActive(MsgActive.PENDING);
                byChatId.get().setLastUpdate(LocalDateTime.now());
                byChatId.get().setLastUpdateBy(Long.parseLong(employeesId));
                chatRepo.save(byChatId.get());
            }
        }
        return true;
    }

    // الموافقة على اغلاق المحادثة او لا
    public Boolean askArchivedChat(Long chatId, ConfirmProcedure confirmProcedure, String token){
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<AppChatMaster> byChatId = chatRepo.findById(chatId);

        AppChatDetails updateCloseChat = messagesRepo.findById(chatId).get();
        updateCloseChat.setConfirmProcedure(confirmProcedure);
        updateCloseChat.setMsgActivity(MsgActivity.NOT_ACTIVE);
        updateCloseChat.setLastUpdate(LocalDateTime.now());
//        updateCloseChat.getLastUpdateBy(Long.parseLong(userTokenId));
        messagesRepo.save(updateCloseChat);
        byChatId.get().setMsgActive(MsgActive.ARCHIVED);
        byChatId.get().setLastUpdate(LocalDateTime.now());
        byChatId.get().setLastUpdateBy(Long.parseLong(userTokenId));
        chatRepo.save(byChatId.get());

        return true;
    }



}
