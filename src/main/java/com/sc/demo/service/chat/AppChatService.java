package com.sc.demo.service.chat;

import com.google.auth.oauth2.JwtClaims;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class AppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    public AppChatMaster createChat(AppChatRequest appChatRequest){
        return null;
    }
}
