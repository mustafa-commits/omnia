package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.chat.WhoIsSender;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.model.users.Token;
import com.sc.demo.repository.chat.ChatRepo;
import com.sc.demo.repository.chat.ChatTokenRepo;
import com.sc.demo.repository.chat.MessagesRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
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
    private ChatTokenRepo chatTokenRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Environment environment;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MessagesRepo messagesRepo;

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(ChatTokenRequest chatTokenRequest) {
        Optional<Token> byToken = chatTokenRepo.findById(chatTokenRequest.userId());

        if (byToken.isPresent()) {
            Token token = byToken.get();
            token.setLastUpdate(LocalDateTime.now());
            token.setToken(chatTokenRequest.token());
            return chatTokenRepo.save(token).getUserId();
        } else {
            Token token = new Token();
            token.setToken(chatTokenRequest.token());
            token.setUserId(chatTokenRequest.userId());
            return chatTokenRepo.save(token).getUserId();
        }
    }

    // جلب المحادثات المفعلة
    public List<DashAppChatResponse> dashPhoneChats(){
        return jdbcClient.sql("""
             SELECT U.GUARDIAN_NAME AS guardianName
                    ,M.CHAT_TITLE AS chatTitle
                    ,D.MESSAGES AS messages
                    ,D.CREATE_DATE AS createDate
             FROM MOBAPP.SC_CHAT_MASTER M
             JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
             JOIN MOBAPP.SC_APP_USERS U ON (M.CREATE_BY = U.USERID)
             WHERE D.MSG_ACTIVITY = 0
             AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            """)
            .query(DashAppChatResponse.class)
            .list();
    }

    // جلب المحادثات المؤرشفة
    public List<DashAppChatResponse> dashPhoneChatsArchived(){
        return jdbcClient.sql("""
             SELECT U.GUARDIAN_NAME AS guardianName
                    ,M.CHAT_TITLE AS chatTitle
                    ,D.MESSAGES AS messages
                    ,D.CREATE_DATE AS createDate
             FROM MOBAPP.SC_CHAT_MASTER M
             JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
             JOIN MOBAPP.SC_APP_USERS U ON (M.CREATE_BY = U.USERID)
             WHERE D.MSG_ACTIVITY = 1
             AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            """)
                .query(DashAppChatResponse.class)
                .list();
    }

    public boolean dashWriteMessages(MessagesRequest messagesRequest, MultipartFile file, MultipartFile voice, String token) {
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
                ? WhoIsSender.USER
                : WhoIsSender.EMPLOYEE;

        AppChatDetails appChatDetails = new AppChatDetails(chatRepo.getReferenceById(messagesRequest.chatId()),
                Long.parseLong(userTokenId), whoIsSender,
                messagesRequest.platform(), messagesRequest.messages().isEmpty() ? newFileName : messagesRequest.messages(),
                messagesRequest.msgType());
        Long detailsChatId = messagesRepo.save(appChatDetails).getDetailsChatId();
        System.out.println(detailsChatId);
        return true;
    }


    public List<MessagesResponse> dashGetMessages(long chatId){
        System.out.println(chatId);
        return jdbcClient.sql("""
                SELECT CASE WHEN MSG_TYPE in (1,2) THEN TO_CHAR(:path) || MESSAGES
                        ELSE MESSAGES END AS messages,
                        WHO_IS_SENDER AS whoIsSender,
                        CREATE_BY AS createBy,
                        CREATE_DATE AS createDate
                FROM MOBAPP.SC_CHAT_DETAILS
                WHERE CHAT_ID = :chatId
                order by CREATE_DATE desc
                """)
                .param("chatId", chatId)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/dashPhotoChat/")
                .query(MessagesResponse.class)
                .list();
    }
}
