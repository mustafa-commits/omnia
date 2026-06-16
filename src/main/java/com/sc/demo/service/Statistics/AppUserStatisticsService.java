package com.sc.demo.service.Statistics;

import com.sc.demo.model.dto.statistics.AppUserStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserStatisticsService {

    @Autowired
    private JdbcClient jdbcClient;

    // جلب المحادثات المفعلة
    public List<AppUserStatisticsResponse> getStatisticsPhoneUsers(){
        return jdbcClient.sql("""
            SELECT U.PHONE AS phone,
                   U.BRANCHES AS branches,
                   U.GUARDIAN_NAME AS guardianName,
                   U.PHONE_TYPE AS phoneType,
                   U.TIME_USED AS timeUsed,
                   P.FAMILY_PERSONS_ID AS familyPersonsId
            FROM MOBAPP.SC_APP_USER U
            LEFT Join AIN_CAPPS.SC_FAMILY_PERSONS P on (P.HEAD_FAMILY_ID = U.HEAD_FAMILY_ID)
            WHERE P.IS_GUARDIAN = 1
            """)
                .query(AppUserStatisticsResponse.class)
                .list();
    }

    // جلب المحادثات المؤرشفة
//    public List<DashAppChatResponse> dashPhoneChatsArchived(){
//        return jdbcClient.sql("""
//            SELECT M.CHAT_ID AS chatId
//                   ,U.GUARDIAN_NAME AS guardianName
//                    ,M.CHAT_TITLE AS chatTitle
//                    ,D.MESSAGES AS messages
//                    ,D.CREATE_DATE AS createDate
//            FROM MOBAPP.SC_CHAT_MASTER M
//            JOIN MOBAPP.SC_CHAT_DETAILS D ON (M.CHAT_ID = D.CHAT_ID)
//            JOIN MOBAPP.SC_APP_USER U ON (M.CREATE_BY = U.USER_ID)
//            WHERE D.MSG_ACTIVITY = 1
//            AND D.CREATE_DATE = (SELECT MAX(CREATE_DATE) FROM MOBAPP.SC_CHAT_DETAILS D1 WHERE D1.CHAT_ID = D.CHAT_ID)
//            """)
//                .query(DashAppChatResponse.class)
//                .list();
//    }
}
