package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.chat.msgType;
import com.sc.demo.model.chat.platform;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.service.chat.AppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class appChatController implements SecuredRestController {

    @Autowired
    private AppChatService appChatService;

    // انشاء محادثة
    @PostMapping("/V1/api/sc/createChat")
    public boolean createChat(@RequestBody appChatRequest appChatRequest){
        return appChatService.createChat(appChatRequest);
    }

    // تخزين Token
    @PostMapping("/V1/api/sc/setChatToken")
    public long setChatToken(@RequestBody chatTokenRequest chatTokenRequest){
        return appChatService.saveToken(chatTokenRequest);
    }

    // اظهار المحادثات الفعالة
    @GetMapping("/V1/api/sc/getPhoneChats")
    public List<appChatResponse> getPhoneChats(@RequestHeader(name = "authorization") String token){
        return appChatService.phoneChats(token);
    }

    // اظهار المحادثات المأرشفة
    @GetMapping("/V1/api/sc/getPhoneChatsArchived")
    public List<appChatResponse> getPhoneChatsArchived(@RequestHeader(name = "authorization") String token){
        return appChatService.PhoneChatsArchived(token);
    }

    // ارسال رسالة
    @PostMapping("/V1/api/sc/writeMessages")
    public boolean writeMessages(@RequestParam Long chatId,
                                @RequestParam Long sender,
                                @RequestParam Long receiver,
                                @RequestParam platform platform,
                                @RequestParam(required = false) String messages,
                                @RequestParam msgType msgType,
                                @RequestParam(required = false) MultipartFile file,
                                @RequestHeader(name = "authorization") String token){
        return appChatService.writeMessages(new messagesRequest(chatId, sender, receiver,
                platform, messages, msgType), file, token);
    }

    // اظهار الرسائل في المحادثات
    @GetMapping("/V1/api/sc/getMessagesChat")
    public List<messagesResponse> getMessages(@RequestParam long chat_id){
        return appChatService.getMessages(chat_id);
    }
}
