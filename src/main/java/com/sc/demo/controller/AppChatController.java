package com.sc.demo.controller;

import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.service.chat.AppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AppChatController {

    @Autowired
    private AppChatService appChatService;

    // انشاء محادثة
    @PostMapping("/V1/api/sc/createChat")
    public AppChatMaster createChat(@RequestBody AppChatRequest appChatRequest){
        return appChatService.createChat(appChatRequest);
    }
}
