package com.sc.demo.controller;

import com.sc.demo.config.OpenApi30Config;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.ReceiverFrom;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.model.dto.Chat.AppChatResponse;
import com.sc.demo.model.dto.Chat.MessagesRequest;
import com.sc.demo.model.dto.Chat.MessagesResponse;
import com.sc.demo.service.chat.AppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AppChatController implements OpenApi30Config {

    @Autowired
    private AppChatService appChatService;

    // انشاء محادثة
    @PostMapping("/V1/api/sc/createChat")
    public AppChatMaster createChat(@RequestBody AppChatRequest appChatRequest){
        return appChatService.createChat(appChatRequest);
    }

    // اظهار المحادثات
    @GetMapping("/V1/api/sc/getPhoneChats")
    public List<AppChatResponse> getPhoneChats(@RequestHeader(name = "authorization") String token){
        return appChatService.phoneChats(token);
    }

    // ارسال رسالة
    @PostMapping("/V1/api/sc/writeMessages")
    public boolean writeMessages(@RequestParam Long chatId,
                                        @RequestParam Long sender,
                                        @RequestParam Long receiver,
                                        @RequestParam ReceiverFrom receiverFrom,
                                        @RequestParam(required = false) String messages,
                                        @RequestParam MsgType msgType,
                                        @RequestParam(required = false) MultipartFile file,
                                        @RequestHeader(name = "authorization") String token){
        return appChatService.writeMessages(new MessagesRequest(chatId, sender, receiver,
                        receiverFrom, messages, msgType), file, token);
    }

    // اظهار الرسائل في المحادثات
    @GetMapping("/V1/api/sc/getMessagesChat")
    public List<MessagesResponse> getMessages(@RequestParam long chat_id){
        return appChatService.getMessages(chat_id);
    }
}
