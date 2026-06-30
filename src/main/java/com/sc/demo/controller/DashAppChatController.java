package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.chat.ConfirmProcedure;
import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.service.chat.DashAppChatService;
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
public class DashAppChatController implements SecuredRestController {

    @Autowired
    private DashAppChatService dashAppChatService;

    @Value("${ATTACHMENT_PATH_CHAT}")
    private String uploadDir;

    @Value("${ATTACHMENT_PATH_VOICE}")
    private String uploadVoice;

    // ارسال رسالة
    @PostMapping("/V1/api/sc/dashWriteMessages")
    public boolean dashWriteMessages(@RequestParam Long chatId,
                                     @RequestParam String messages,
                                     @RequestParam MsgType msgType,
                                     @RequestParam(required = false) MultipartFile file,
                                     @RequestParam(required = false) MultipartFile voice,
                                     @RequestHeader(name = "authorization") String token){
        return dashAppChatService.dashWriteMessages(new MessagesRequest(chatId, messages, msgType), file, voice, token);
    }

    // اظهار الرسائل في الداش بورد
    @GetMapping("/V1/api/sc/getDashMessages")
    public List<MessagesResponse> getDashMessages(@RequestParam Long chatId){
        System.out.println(chatId);
        return dashAppChatService.getDashMessages(chatId);
    }

    // اظهار الصور والصوت
    @GetMapping("/V1/api/sc/dashPhotoChat/{filename:.+}")
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
    @PostMapping("/V1/api/sc/requestArchivedChat")
    public Boolean requestArchivedChat(@RequestParam Long chatId,
                                       @RequestHeader(name = "authorization") String token){
        return dashAppChatService.requestArchivedChat(chatId, token);
    }

    // الموافقة على اغلاق المحادثة او لا
    @PutMapping("/V1/api/sc/askArchivedChat")
    public Boolean askArchivedChat(@RequestParam Long chatId,
                                   @RequestParam ConfirmProcedure confirmProcedure,
                                   @RequestHeader(name = "authorization") String token){
        return dashAppChatService.askArchivedChat(chatId, confirmProcedure, token);
    }
}

