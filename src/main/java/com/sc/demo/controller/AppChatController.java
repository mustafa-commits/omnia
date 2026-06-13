package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.service.chat.AppChatService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AppChatController implements SecuredRestController {

    @Autowired
    private AppChatService appChatService;

    @Value("${ATTACHMENT_PATH_CHAT}")
    private String uploadDir;

    @Value("${ATTACHMENT_PATH_VOICE}")
    private String uploadVoice;

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
                                 @RequestParam Platform platform,
                                 @RequestParam String messages,
                                 @RequestParam MsgType msgType,
                                 @RequestParam Long createBy,
                                 @RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) MultipartFile voice,
                                 @RequestHeader(name = "authorization") String token){
        return appChatService.writeMessages(new MessagesRequest(chatId,
                platform, messages, msgType, createBy), file, voice, token);
    }

    // اظهار الرسائل في المحادثات
    @GetMapping("/V1/api/sc/getMessagesChat")
    public List<MessagesResponse> getMessages(@RequestParam long chatId){
        System.out.println(chatId);
        return appChatService.getMessages(chatId);
    }

    @GetMapping("/V1/api/sc/photoChat/{filename:.+}")
    public void serveFile(
            @PathVariable String filename,
            @RequestParam MsgType msgType,
            HttpServletResponse response
    ) throws IOException {
        var file = Paths.get(uploadDir, filename);
        var voice = Paths.get(uploadVoice, filename);
        System.out.println("Looking for file at: " + file.toAbsolutePath());
        if (Files.exists(file) && msgType == MsgType.IMAGE) {
            response.setContentType(Files.probeContentType(file));
            response.setContentLengthLong(Files.size(file));
            Files.copy(file, response.getOutputStream());
            return;
        }
        if (Files.exists(voice) && msgType == MsgType.VOICE){
            response.setContentType(Files.probeContentType(voice));
            response.setContentLengthLong(Files.size(voice));
            Files.copy(voice, response.getOutputStream());
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
