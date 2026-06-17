package com.sc.demo.service.chat;

import com.sc.demo.model.chat.AppChatDetails;
import com.sc.demo.model.chat.AppChatMaster;
import com.sc.demo.model.dto.chat.*;
import com.sc.demo.model.familyInfo.FamilyInfo;
import com.sc.demo.repository.chat.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DashAppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepo chatRepo;

    // جلب المحادثات المفعلة
    public List<DashAppChatResponse> dashPhoneChats(){
        return jdbcClient.sql("""
             SELECT M.CHAT_ID AS chatId
                   ,U.GUARDIAN_NAME AS guardianName
                   ,M.CHAT_TITLE AS chatTitle
                   ,D.MESSAGES AS messages
                   ,D.CREATE_DATE AS createDate
             FROM MOBAPP.SC_CHAT_MASTER M
             LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
             LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
             WHERE D.MSG_ACTIVITY = 0
             AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
             ORDER BY M.CHAT_ID DESC
            """)
            .query(DashAppChatResponse.class)
            .list();
    }

    // جلب المحادثات المؤرشفة
    public List<DashAppChatResponse> dashPhoneChatsArchived(){
        return jdbcClient.sql("""
            SELECT M.CHAT_ID AS chatId
                  ,U.GUARDIAN_NAME AS guardianName
                  ,M.CHAT_TITLE AS chatTitle
                  ,D.MESSAGES AS messages
                  ,D.CREATE_DATE AS createDate
            FROM MOBAPP.SC_CHAT_MASTER M
            LEFT JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
            LEFT JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
            WHERE D.MSG_ACTIVITY = 1
            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            ORDER BY M.CHAT_ID DESC
            """)
                .query(DashAppChatResponse.class)
                .list();
    }

    public Boolean changeChatActivity(Long chatId){
        Optional<AppChatMaster> byChatId = chatRepo.findById(chatId);
        if (byChatId.isPresent()) {
            jdbcClient.sql("""
                            UPDATE MOBAPP.SC_CHAT_DETAILS D
                            SET D.MSG_ACTIVITY = 1
                            WHERE D.CHAT_ID = :chatId
                            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
                            """)
                    .param("chatId", chatId)
                    .update();
            return true;
        }
        return false;
    }

}
