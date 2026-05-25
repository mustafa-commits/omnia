package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.model.dto.Chat.AppChatResponse;
import com.sc.demo.model.dto.Chat.MessagesRequest;
import com.sc.demo.model.dto.Chat.MessagesResponse;
import com.sc.demo.repository.Chat.MessagesRepo;
import com.sc.demo.repository.Chat.ChatRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

        for (AppChatDetails a : appChatRequest.appChatDetails()){
                messagesRepo.save(new AppChatDetails(a.getSender(), a.getReceiver(),
                        a.getReceiverFrom(), a.getMessages(), appChatMaster));
        }
        return appChatMaster;
    }

    public List<AppChatResponse> phoneChats(long user_id){
        return jdbcClient.sql("""
                SELECT M.CHAT_TITLE AS chatTitle,
                       D.MESSAGES AS messages,
                       M.CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_MASTER M
                LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON M.CHAT_ID = D.CHAT_ID
                WHERE M.USER_ID = :user_id
                """)
                .param("user_id", user_id)
                .query(AppChatResponse.class)
                .list();
    }

    public AppChatDetails writeMessages(MessagesRequest messagesRequest, String token){
        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                messagesRequest.sender(), messagesRequest.receiver(),
                messagesRequest.receiverFrom(), messagesRequest.messages(),
                messagesRequest.msgType());

        var requestId = tokenService.decodeToken(token.substring(7)).getClaim("requestId");
        var headId = tokenService.decodeToken(token.substring(7)).getClaim("headId");
        var userId = tokenService.decodeToken(token.substring(7)).getSubject();

        messagesRequest.messages() = String originalFilename = file.getOriginalFilename();
        String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = environment.getProperty("ATTACHMENT_PATH") + newFilename;


        appChatDetails = messagesRepo.save(appChatDetails);
        if(messagesRequest.msgType() == MsgType.IMAGE) {
            file.transferTo(new File(filePath));

        }

//        if (file != null)
//            try {
//                String originalFilename = file.getOriginalFilename();
//                String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
//                String filePath = environment.getProperty("ATTACHMENT_PATH") + newFilename;
//                appChatAttachmentsRepo.save(newFilename);
//                file.transferTo(new File(filePath));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

        return appChatDetails;
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
