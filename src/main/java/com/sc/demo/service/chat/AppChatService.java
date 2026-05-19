package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.repository.ChatDetailsRepo;
import com.sc.demo.repository.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class AppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private ChatDetailsRepo chatDetailsRepo;

    public AppChatMaster createChat(AppChatRequest appChatRequest){
        AppChatMaster appChatMaster = new AppChatMaster(appChatRequest.userId(),
                appChatRequest.chatTitle(), appChatRequest.chatDescription());

        appChatMaster = chatRepo.save(appChatMaster);

        for (AppChatDetails a : appChatRequest.appChatDetails()){
                chatDetailsRepo.save(new AppChatDetails(a.getSender(),a.getReceiver(),
                        a.getMsgType(), a.getSeenAt(), a.getCreateBy(), appChatMaster));
        }
        return appChatMaster;
    }
}
