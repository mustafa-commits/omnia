package com.sc.demo.service.chat;

import com.sc.demo.model.chat.*;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.repository.chat.MessagesRepo;
import com.sc.demo.repository.chat.ChatRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class AppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MessagesRepo messagesRepo;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenService tokenService;

    public boolean createChat(AppChatRequest appChatRequest){
        AppChatMaster appChatMaster = new AppChatMaster(appChatRequest.createBy(), appChatRequest.chatTitle());

        appChatMaster = chatRepo.save(appChatMaster);

        Long userIdSender = appChatRequest.createBy();

        AppChatDetails appChatDetails = appChatRequest.appChatDetails();
        WhoIsSender whoIsSender = Platform.APP.equals(appChatRequest.appChatDetails().getPlatform())
                ? WhoIsSender.USER
                : WhoIsSender.EMPLOYEE;

        messagesRepo.save(new AppChatDetails(userIdSender, whoIsSender,
                                            appChatDetails.getPlatform(), appChatDetails.getMessages(),
                                            appChatDetails.getCreateBy(), appChatMaster));

        AppChatDetails welcomeMessage = new AppChatDetails();
        welcomeMessage.setChatApp(appChatMaster);
        welcomeMessage.setUserIdSender(0L);
        welcomeMessage.setPlatform(Platform.DASHBOARD);
        welcomeMessage.setMessages("""
                السلام عليكم ورحمة الله وبركاته
                نود أن نلفت عنايتكم ألى ان كادر الدعم الفني متواجدين للإجابة على استفساراتكم من الساعة ( 8:00 )
                 صباحاً لغاية الساعة ( 4:00 ) مساءً  طيلة أيام الأسبوع باستثناء يومي الخميس الجمعة,
                لطفاً يرجى ارسال استفسارك بالتفصيل ليتسنى لنا الإجابة على طلبكم بأسرع وقت ممكن ,
                مع الشكر والتقدير
                """);

        messagesRepo.save(welcomeMessage);

        return true;
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
//    public long saveToken(ChatTokenRequest chatTokenRequest) {
//        Optional<ChatToken> byToken = chatTokenRepo.findById(chatTokenRequest.chatId());
//
//        if (byToken.isPresent()){
//            ChatToken chatToken = byToken.get();
//            chatToken.setLastUpdate(LocalDate.now());
//            chatToken.setToken(chatTokenRequest.token());
//            return chatTokenRepo.save(chatToken).getChatId();
//        } else {
//            ChatToken chatToken = byToken.get();
//            chatToken.setToken(chatTokenRequest.token());
//            chatToken.setChatId(chatTokenRequest.chatId());
//            return chatTokenRepo.save(chatToken).getChatId();
//        }
//    }

    // جلي المحادثات المفعلة
    public List<AppChatResponse> phoneChats(String token){
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userId);

        return jdbcClient.sql("""
                SELECT M.CHAT_ID AS chatId,
                       M.CHAT_TITLE AS chatTitle,
                       D.MESSAGES AS messages,
                       M.CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.USER_ID = :user_id
                AND D.MSG_ACTIVITY = 0
                AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D.CHAT_ID = D1.CHAT_ID)
                """)
                .param("user_id", userId)
                .query(AppChatResponse.class)
                .list();
    }

    public List<AppChatResponse> PhoneChatsArchived(String token){
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userId);

        return jdbcClient.sql("""
                SELECT M.CHAT_ID AS chatId,
                       M.CHAT_TITLE AS chatTitle,
                       D.MESSAGES AS messages,
                       M.CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.USER_ID = :user_id
                AND D.MSG_ACTIVITY = 1
                AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D.CHAT_ID = D1.CHAT_ID)
                """)
                .param("user_id", userId)
                .query(AppChatResponse.class)
                .list();
    }

    public boolean writeMessages(MessagesRequest messagesRequest, MultipartFile file, MultipartFile voice, String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
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
                Long.parseLong(userId), whoIsSender,
                messagesRequest.platform(), messagesRequest.messages().isEmpty() ? newFileName : messagesRequest.messages(),
                messagesRequest.msgType(), messagesRequest.createBy());
        Long detailsChatId = messagesRepo.save(appChatDetails).getDetailsChatId();
        System.out.println(detailsChatId);
        return true;
    }


    public List<MessagesResponse> getMessages(long chatId){
        System.out.println(chatId);
        return jdbcClient.sql("""
                SELECT CASE WHEN MSG_TYPE in (1,2) THEN TO_CHAR(:path) || MESSAGES
                        ELSE MESSAGES END AS messages,
                        WHO_IS_SENDER AS whoIsSender,
                        USER_ID_SENDER AS useridSender,
                        CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_DETAILS
                WHERE CHAT_ID = :chat_id
                order by CREATE_DATE desc
                """)
                .param("chat_id", chatId)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/photoChat/")
                .query(MessagesResponse.class)
                .list();
    }
}
