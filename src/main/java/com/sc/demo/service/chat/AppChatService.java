package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.ReceiverFrom;
import com.sc.demo.model.dto.Chat.*;
import com.sc.demo.repository.Chat.MessagesRepo;
import com.sc.demo.repository.Chat.ChatRepo;
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


    public AppChatMaster createChat(AppChatRequest appChatRequest){
        AppChatMaster appChatMaster = new AppChatMaster(appChatRequest.userId(), appChatRequest.chatTitle());

        appChatMaster = chatRepo.save(appChatMaster);

        AppChatDetails appChatDetails = appChatRequest.appChatDetails();
        messagesRepo.save(new AppChatDetails(appChatDetails.getSender(), appChatDetails.getReceiver(),
                        appChatDetails.getReceiverFrom(), appChatDetails.getMessages(), appChatMaster));

        AppChatDetails welcomeMessage = new AppChatDetails();
        welcomeMessage.setChatApp(appChatMaster);
        welcomeMessage.setSender(0L);
        welcomeMessage.setReceiverFrom(ReceiverFrom.DASHBOARD);
        welcomeMessage.setMessages("""
                السلام عليكم ورحمة الله وبركاته
                نود أن نلفت عنايتكم ألى ان كادر الدعم الفني متواجدين للإجابة على استفساراتكم من الساعة ( 8:00 )
                 صباحاً لغاية الساعة ( 4:00 ) مساءً  طيلة أيام الأسبوع باستثناء يومي الخميس الجمعة,
                لطفاً يرجى ارسال استفسارك بالتفصيل ليتسنى لنا الإجابة على طلبكم بأسرع وقت ممكن ,
                مع الشكر والتقدير
                """);

        messagesRepo.save(welcomeMessage);

        return appChatMaster;
    }



    public List<AppChatResponse> phoneChats(String token){
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        System.out.println(userId);

        return jdbcClient.sql("""
                SELECT M.CHAT_TITLE AS chatTitle,
                       D.MESSAGES AS messages,
                       M.CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.USER_ID = :user_id
                AND D.MSG_TYPE = 0
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
                SELECT M.CHAT_TITLE AS chatTitle,
                       D.MESSAGES AS messages,
                       M.CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_MASTER M
                JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                WHERE M.USER_ID = :user_id
                AND D.MSG_TYPE = 1
                AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D.CHAT_ID = D1.CHAT_ID)
                """)
                .param("user_id", userId)
                .query(AppChatResponse.class)
                .list();
    }

    public boolean writeMessages(MessagesRequest messagesRequest, MultipartFile file, String token) {
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
        String newFilename = null;


        if(messagesRequest.msgType() == MsgType.IMAGE) {
            try {
                String originalFilename = file.getOriginalFilename();
                 newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
                String filePath = environment.getProperty("ATTACHMENT_PATH") + newFilename;
                file.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                Long.parseLong(userId), messagesRequest.receiver(),
                messagesRequest.receiverFrom(), messagesRequest.messages().isEmpty() ? newFilename : messagesRequest.messages(),
                messagesRequest.msgType() == null ? MsgType.MESSAGE : messagesRequest.msgType());
        System.out.println(messagesRepo.save(appChatDetails).getDetailsChatId());
        System.out.println(messagesRepo.save(appChatDetails).getMessages());
        return true;
    }


    public List<MessagesResponse> getMessages(long chat_id){
        return jdbcClient.sql("""
                SELECT MESSAGES,
                       RECEIVER,
                       SENDER,
                       CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_DETAILS
                WHERE CHAT_ID = :chat_id
                order by CREATE_DATE desc
                """)
                .param("chat_id", chat_id)
                .query(MessagesResponse.class)
                .list();
    }
}
