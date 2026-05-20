package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.model.dto.Chat.AppChatResponse;
import com.sc.demo.model.dto.Chat.MessagesRequest;
import com.sc.demo.repository.Chat.MessagesRepo;
import com.sc.demo.repository.Chat.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MessagesRepo messagesRepo;

    public AppChatMaster createChat(AppChatRequest appChatRequest){
        AppChatMaster appChatMaster = new AppChatMaster(appChatRequest.userId(),
                appChatRequest.chatTitle(), appChatRequest.chatDescription());

        appChatMaster = chatRepo.save(appChatMaster);

        for (AppChatDetails a : appChatRequest.appChatDetails()){
                messagesRepo.save(new AppChatDetails(a.getSender(),a.getReceiver(), a.getMessages(), appChatMaster));
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

    public AppChatDetails writeMessages(MessagesRequest messagesRequest){
        Optional<AppChatDetails> byChatId = messagesRepo.findById(messagesRequest.receiver());

        AppChatDetails appChatDetails = new AppChatDetails(messagesRequest.sender(),
                messagesRequest.receiver(), messagesRequest.messages());

        appChatDetails = messagesRepo.save(appChatDetails);

        return appChatDetails;
    }
}
