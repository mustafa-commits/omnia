package com.sc.demo.controller;

import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.service.chat.DashAppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DashAppChatController {

    @Autowired
    private DashAppChatService dashAppChatService;

    // ارسال رسالة
    @PostMapping("/V1/api/sc/dashWriteMessages")
    public boolean dashWriteMessages(@RequestParam Long chatId,
                                     @RequestParam Platform platform,
                                     @RequestParam String messages,
                                     @RequestParam MsgType msgType,
                                     @RequestParam(required = false) MultipartFile file,
                                     @RequestParam(required = false) MultipartFile voice,
                                     @RequestHeader(name = "authorization") String token){
        return dashAppChatService.dashWriteMessages(new MessagesRequest(chatId,
                platform, messages, msgType), file, voice, token);
    }

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
    @PutMapping("/V1/api/sc/requestArchivedChat")
    public Boolean requestArchivedChat(@RequestParam Long chatId){
        return dashAppChatService.requestArchivedChat(chatId);
    }
}

