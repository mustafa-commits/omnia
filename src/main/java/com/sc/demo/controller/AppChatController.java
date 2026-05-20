package com.sc.demo.controller;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import com.sc.demo.model.dto.Chat.AppChatResponse;
import com.sc.demo.model.dto.Chat.MessagesRequest;
import com.sc.demo.service.chat.AppChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // اظهار المحادثات
    @GetMapping("/V1/api/sc/getPhoneChats")
    public List<AppChatResponse> getPhoneChats(@RequestParam long user_id){
        return appChatService.phoneChats(user_id);
    }

    // ارسال رسالة
    @PostMapping("/V1/api/sc/writeMessages")
    public AppChatDetails writeMessages(@RequestParam Long sender,
                                        @RequestParam Long receiver,
                                        @RequestParam String messages){
        return appChatService.writeMessages(new MessagesRequest(sender, receiver, messages));
    }
}
