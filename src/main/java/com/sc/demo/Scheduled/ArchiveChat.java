package com.sc.demo.Scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;

public class ArchiveChat {

    @Autowired
    private JdbcClient jdbcClient;

    @Scheduled(cron = "0 0 */6 * * *")
    private void archiveChat() {
        jdbcClient.sql("""
            UPDATE MOBAPP.SC_CHAT_MASTER M
            SET M.MSG_ACTIVE = 2
            WHERE M.MSG_ACTIVE = 1
            AND EXISTS (
                SELECT 1
                FROM MOBAPP.SC_CHAT_DETAILS D
                WHERE D.CHAT_ID = M.CHAT_ID
                AND D.MSG_ACTIVITY = 2
                AND MAX(D.CREATE_DATE) <= SYSDATE + interval '1' day
            )
            """)
                .update();
    }
}
