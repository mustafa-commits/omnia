package com.sc.demo.service.chat;

import com.sc.demo.model.dto.chat.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashAppChatService {

    @Autowired
    private JdbcClient jdbcClient;

    // جلب المحادثات المفعلة
    public List<DashAppChatResponse> dashPhoneChats(){
        return jdbcClient.sql("""
             SELECT M.CHAT_ID AS chatId
                                ,U.GUARDIAN_NAME AS guardianName
                                 ,M.CHAT_TITLE AS chatTitle
                                 ,D.MESSAGES AS messages
                                 ,D.CREATE_DATE AS createDate
                         FROM MOBAPP.SC_CHAT_MASTER M
                         JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                         JOIN MOBAPP.SC_APP_USERS U ON (M.CREATE_BY = U.USERID)
                         WHERE D.MSG_ACTIVITY = 0
                         AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
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
                        JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
                        JOIN MOBAPP.SC_APP_USERS U ON (M.CREATE_BY = U.USERID)
                        WHERE D.MSG_ACTIVITY = 1
                        AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
            """)
                .query(DashAppChatResponse.class)
                .list();
    }

    public Boolean changeChatActivity(){
        return true;
    }

}
