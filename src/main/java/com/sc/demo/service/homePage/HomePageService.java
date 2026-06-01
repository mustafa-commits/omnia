package com.sc.demo.service.homePage;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.chat.ReceiverFrom;
import com.sc.demo.model.dto.Chat.AppChatRequest;
import org.springframework.stereotype.Service;

@Service
public class HomePageService {

//    public AppChatMaster addHomePagePhoto(AppChatRequest appChatRequest){
//        AppChatMaster appChatMaster = new AppChatMaster(appChatRequest.userId(), appChatRequest.chatTitle());
//
//        appChatMaster = chatRepo.save(appChatMaster);
//
//        AppChatDetails appChatDetails = appChatRequest.appChatDetails();
//        messagesRepo.save(new AppChatDetails(appChatDetails.getSender(), appChatDetails.getReceiver(),
//                appChatDetails.getReceiverFrom(), appChatDetails.getMessages(), appChatMaster));
//
//        AppChatDetails welcomeMessage = new AppChatDetails();
//        welcomeMessage.setChatApp(appChatMaster);
//        welcomeMessage.setSender(0L);
//        welcomeMessage.setReceiverFrom(ReceiverFrom.DASHBOARD);
//        welcomeMessage.setMessages("""
//                السلام عليكم ورحمة الله وبركاته
//                نود أن نلفت عنايتكم ألى ان كادر الدعم الفني متواجدين للإجابة على استفساراتكم من الساعة ( 8:00 )
//                 صباحاً لغاية الساعة ( 4:00 ) مساءً  طيلة أيام الأسبوع باستثناء يومي الخميس الجمعة,
//                لطفاً يرجى ارسال استفسارك بالتفصيل ليتسنى لنا الإجابة على طلبكم بأسرع وقت ممكن ,
//                مع الشكر والتقدير
//                """);
//
//        messagesRepo.save(welcomeMessage);
//
//        return appChatMaster;
//    }
}
