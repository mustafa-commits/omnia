package com.sc.demo.controller;

import com.sc.demo.model.dto.chat.*;
import com.sc.demo.service.chat.DashAppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DashAppChatController {

    @Autowired
    private DashAppChatService dashAppChatService;

    // اظهار المحادثات الفعالة
    @GetMapping("/V1/api/sc/getDashPhoneChats")
    public List<DashAppChatResponse> getDashPhoneChats(){
        return dashAppChatService.dashPhoneChats();
    }

    // اظهار المحادثات المأرشفة
    @GetMapping("/V1/api/sc/getDashPhoneChatsArchived")
    public List<DashAppChatResponse> getPhoneChatsArchived(){
        return dashAppChatService.dashPhoneChatsArchived();
    }

    // تحويل المحادثات من فعالة الى مؤرشفة
    @PutMapping("/V1/api/sc/changeChatActivity")
    public Boolean changeChatActivity(){
        return dashAppChatService.changeChatActivity();
    }
}

