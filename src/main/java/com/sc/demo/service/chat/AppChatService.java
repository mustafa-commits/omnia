package com.sc.demo.service.chat;

import com.google.firebase.messaging.*;
import com.sc.demo.model.chat.*;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.repository.chat.AppChatDetailsRepo;
import com.sc.demo.repository.chat.TokenRepo;
import com.sc.demo.repository.chat.ChatRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class AppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private AppChatDetailsRepo appChatDetailsRepo;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenService tokenService;

    // انشاء محادثة
    public boolean createChat(AppChatRequest appChatRequest, String token){
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();

        AppChatMaster appChatMaster = new AppChatMaster(Long.parseLong(userTokenId), appChatRequest.chatTitle());

        appChatMaster = chatRepo.save(appChatMaster);

        AppChatDetails appChatDetails = appChatRequest.appChatDetails();

        appChatDetailsRepo.save(new AppChatDetails(Long.parseLong(userTokenId), WhoIsSender.APPUSER, Platform.APP,
                appChatDetails.getMessages(),appChatMaster));

        // رسالة تلقائية عند انشاء محادثة
        AppChatDetails welcomeMessage = new AppChatDetails();
        welcomeMessage.setChatApp(appChatMaster);
        welcomeMessage.setCreateBy(0L);
        welcomeMessage.setPlatform(Platform.SYSTEM);
        welcomeMessage.setMessages("""
                السلام عليكم ورحمة الله وبركاته
                نود أن نلفت عنايتكم ألى ان كادر الدعم الفني متواجدين للإجابة على استفساراتكم من الساعة ( 8:00 )
                صباحاً لغاية الساعة ( 4:00 ) مساءً  طيلة أيام الأسبوع باستثناء يومي الخميس الجمعة,
                لطفاً يرجى ارسال استفسارك بالتفصيل ليتسنى لنا الإجابة على طلبكم بأسرع وقت ممكن ,
                مع الشكر والتقدير
                """);
        appChatDetailsRepo.save(welcomeMessage);
        return true;
    }

    // جلب المحادثات المفعلة في التطبيق
    public List<AppChatResponse> phoneChats(String token){
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userTokenId);

        return jdbcClient.sql("""
                SELECT M.CHAT_ID AS chatId
                      ,M.CHAT_TITLE AS chatTitle
                      ,D.MESSAGES AS messages
                      ,M.CREATE_DATE AS createDate
                      ,M.MSG_ACTIVE AS msgActive
                      ,D.MSG_ACTIVITY AS msgActivity
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.CREATE_BY = :userId
                AND M.MSG_ACTIVE IN (0, 1)
                AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D.CHAT_ID = D1.CHAT_ID)
                ORDER BY M.CREATE_DATE DESC
                """)
                .param("userId", userTokenId)
                .query(AppChatResponse.class)
                .list();
    }

    // جلب المحادثات المؤرشفة في التطبيق
    public List<AppChatResponse> PhoneChatsArchived(String token){
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userTokenId);

        return jdbcClient.sql("""
                SELECT M.CHAT_ID AS chatId
                      ,M.CHAT_TITLE AS chatTitle
                      ,D.MESSAGES AS messages
                      ,M.CREATE_DATE AS createDate
                      ,M.MSG_ACTIVE AS msgActive
                      ,D.MSG_ACTIVITY AS msgActivity
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.CREATE_BY = :userId
                AND M.MSG_ACTIVE = 2
                AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D.CHAT_ID = D1.CHAT_ID)
                ORDER BY M.CREATE_DATE DESC
                """)
                .param("userId", userTokenId)
                .query(AppChatResponse.class)
                .list();
    }

    // ارسال رسالة في التطبيق
    public boolean writeMessages(MessagesRequest messagesRequest, MultipartFile file, MultipartFile voice, String token) {
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
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
                ? WhoIsSender.APPUSER
                : WhoIsSender.USERDASHBOARD;

        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                Long.parseLong(userTokenId), whoIsSender,
                messagesRequest.platform(), messagesRequest.messages().isEmpty() ? newFileName : messagesRequest.messages(),
                messagesRequest.msgType());

        appChatDetailsRepo.save(appChatDetails).getDetailsChatId();
        return true;
    }


    public List<MessagesResponse> getMessages(Long chatId){
        jdbcClient.sql("""
                UPDATE MOBAPP.SC_CHAT_DETAILS
                SET SEEN_AT = SYSDATE
                WHERE CHAT_ID = :chatId
                AND PLATFORM IN (1, 2)
                AND SEEN_AT IS NULL
                """)
                .param("chatId",chatId)
                .update();

        System.out.println(chatId);
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

}
