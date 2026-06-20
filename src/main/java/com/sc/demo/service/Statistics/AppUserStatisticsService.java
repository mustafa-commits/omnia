package com.sc.demo.service.Statistics;

import com.sc.demo.model.dto.statistics.AppUserStatisticsResponse;
import com.sc.demo.model.dto.statistics.StatisticsUsesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserStatisticsService {

    @Autowired
    private JdbcClient jdbcClient;

    // جلب المحادثات المفعلة
    public List<AppUserStatisticsResponse> getStatisticsPhoneUsers(){
        return jdbcClient.sql("""
            SELECT U.PHONE AS phone,
                   GL_BRAN.ARABIC_DESCRIPTION AS branches,
                   U.GUARDIAN_NAME AS guardianName,
                   U.PHONE_TYPE AS phoneType,
                   U.TIME_USED AS timeUsed,
                   P.FAMILY_PERSONS_ID AS familyPersonsId
            FROM MOBAPP.SC_APP_USER U
            LEFT Join AIN_CAPPS.SC_FAMILY_PERSONS P on (P.HEAD_FAMILY_ID = U.HEAD_FAMILY_ID)
            LEFT JOIN AIN_CAPPS.GL_ACC_DETAILS GL_BRAN ON (GL_BRAN.VALUE_CODE = U.BRANCHES AND GL_BRAN.SEGMENT_ID = 1)
            WHERE P.IS_GUARDIAN = 1
            ORDER BY U.TIME_USED
            """)
                .query(AppUserStatisticsResponse.class)
                .list();
    }

    // جلب المحادثات المؤرشفة
    public StatisticsUsesResponse getStatisticsUses(){

        Optional<StatisticsUsesResponse> statistic = jdbcClient.sql("""
            SELECT  COUNT(U.USER_ID) AS totalUsers,
                    COUNT(CASE WHEN TRUNC(U.CREATE_DATE) = TRUNC(SYSDATE) THEN 1 END) AS usersToday,
                    COUNT(CASE WHEN TRUNC(U.TIME_USED) = TRUNC(SYSDATE) THEN 1 END) AS downloadsToday,
                    ROUND(COUNT(CASE WHEN UPPER(U.PHONE_TYPE) = 0 THEN 1 END) * 100.0 / NULLIF(COUNT(U.USER_ID), 0), 2) AS iosPct,
                    ROUND(COUNT(CASE WHEN UPPER(U.PHONE_TYPE) = 1 THEN 1 END) * 100.0 / NULLIF(COUNT(U.USER_ID), 0), 2) AS androidPct
            FROM MOBAPP.SC_APP_USER U
            """)
                .query(StatisticsUsesResponse.class)
                .optional();

        return statistic.orElse(null);
    }
}
