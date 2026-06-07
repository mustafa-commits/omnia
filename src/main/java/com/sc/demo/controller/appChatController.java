package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;
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
    public boolean createChat(@RequestBody AppChatRequest appChatRequest){
        return appChatService.createChat(appChatRequest);
    }

//    // تخزين Token
//    @PostMapping("/V1/api/sc/setChatToken")
//    public long setChatToken(@RequestBody ChatTokenRequest chatTokenRequest){
//        return appChatService.saveToken(chatTokenRequest);
//    }

    // اظهار المحادثات الفعالة
    @GetMapping("/V1/api/sc/getPhoneChats")
    public List<AppChatResponse> getPhoneChats(@RequestHeader(name = "authorization") String token){
        return appChatService.phoneChats(token);
    }

    // اظهار المحادثات المأرشفة
    @GetMapping("/V1/api/sc/getPhoneChatsArchived")
    public List<AppChatResponse> getPhoneChatsArchived(@RequestHeader(name = "authorization") String token){
        return appChatService.PhoneChatsArchived(token);
    }

    // ارسال رسالة
    @PostMapping("/V1/api/sc/writeMessages")
    public boolean writeMessages(@RequestParam Long chatId,
                                 @RequestParam Long userIdSender,
                                 @RequestParam Platform platform,
                                 @RequestParam(required = false) String messages,
                                 @RequestParam MsgType msgType,
                                 @RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) MultipartFile voice,
                                 @RequestHeader(name = "authorization") String token){
        return appChatService.writeMessages(new MessagesRequest(chatId, userIdSender,
                platform, messages, msgType), file, voice, token);
    }

    // اظهار الرسائل في المحادثات
    @GetMapping("/V1/api/sc/getMessagesChat")
    public List<MessagesResponse> getMessages(@RequestParam long chatId){
        return appChatService.getMessages(chatId);
    }
}
