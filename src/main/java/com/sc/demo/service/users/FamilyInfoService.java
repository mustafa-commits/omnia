package com.sc.demo.service.users;

import com.sc.demo.model.dto.FamilyHealthStatusInfoResponse;
import com.sc.demo.model.dto.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfoHousingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FamilyInfoService {

    @Autowired
    private JdbcClient jdbcClient;

    // المعلومات العائلة الاساسية
    public FamilyInfoBasicResponse getFamilyBasicInfo(Long P_FAMILY_NO){
        Optional<FamilyInfoBasicResponse> userData = jdbcClient.sql("""
                        SELECT  F.OLD_FAMILY_NO AS FamilyNo
                                ,H.FOLLOW_ID AS FollowId
                                ,R.FAMILY_PERSON_ID AS FamilyPersonId
                                ,TRIM(
                                     REGEXP_REPLACE(
                                         COALESCE(H.PERSON_NAME_FIRST, '') || ' ' ||
                                         COALESCE(H.PERSON_NAME_SECOND, '') || ' ' ||
                                         COALESCE(H.PERSON_NAME_THIRD, '') || ' ' ||
                                         COALESCE(H.PERSON_NAME_FOURTH, ''),
                                         '\\s+', ' '
                                     )
                                 ) AS PersonFullName
                                ,H.RELATION_ID AS RelationId
                                ,FLOOR(MONTHS_BETWEEN(SYSDATE, H.BIRTH_DATE) / 12) AS Age
                                ,R.PHONE_1 AS Phone1
                                ,R.PHONE_2 AS Phone2
                                ,R.PHONE_3 AS Phone3
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                                LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                                LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                                LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                        WHERE D.FOLLOW_DESCION_DATE = (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                                       FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                                            LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                                               ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                                           LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                                                ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                                       WHERE R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                                           AND F.OLD_FAMILY_NO = F1.OLD_FAMILY_NO)
                         AND F.OLD_FAMILY_NO = :P_FAMILY_NO
                         AND D.FOLLOW_DESCION_STATUS = 2
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                    .query(FamilyInfoBasicResponse.class)
                    .optional();

        if (userData.isPresent())
            return userData.get();
        else
            return null;
    }

    //   معلومات سكن العائلة وفرع التسجيل
    public FamilyInfoHousingResponse getFamilyHousingInfo(Long P_FAMILY_NO){
        Optional<FamilyInfoHousingResponse> housingData = jdbcClient.sql("""
                        SELECT G.GOVERNORATE_NAME AS GovernorateName
                              ,J.JUDGE_NAME AS JudgeName
                              ,C.CITY_NAME AS CityName
                              ,CH.NEIGHBORHOOD AS Neighborhood
                              ,CH.NEARSET_ADDRESS AS NearsetAddress
                              ,GL.ARABIC_DESCRIPTION AS BranchName
                              ,GL2.ARABIC_DESCRIPTION AS OfficeName
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON D.FOLLOW_ID = F.FOLLOW_ID
                              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON F.AID_REQUEST_ID = R.AID_REQUEST_ID
                              LEFT JOIN AIN_CAPPS.SC_CASE_HOUSE_FOLLOW CH ON CH.FOLLOW_ID = D.FOLLOW_ID
                              LEFT JOIN AIN_CAPPS.FND_GOVERNORATES G ON G.GOVERNORATE_ID = CH.GOVERNEMENT_ID
                              LEFT JOIN AIN_CAPPS.FND_JUDGES J ON J.JUDGE_ID = CH.JUDGE_ID
                              LEFT JOIN AIN_CAPPS.FND_CITIES C ON C.CITY_ID = CH.CITY_ID
                              LEFT JOIN AIN_CAPPS.GL_ACC_DETAILS GL ON GL.VALUE_CODE = F.ORG_ID and GL.SEGMENT_ID = 1
                              LEFT JOIN AIN_CAPPS.GL_ACC_DETAILS GL2 ON GL2.VALUE_CODE = F.ORG_OFFICE_ID and GL2.SEGMENT_ID = 10
                        WHERE D.FOLLOW_DESCION_DATE = (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                                     FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                                          LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                                             ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                                         LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                                              ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                                     WHERE R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                                         AND F.OLD_FAMILY_NO = F1.OLD_FAMILY_NO)
                        AND F.OLD_FAMILY_NO = :P_FAMILY_NO
                        AND D.FOLLOW_DESCION_STATUS = 2
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                .query(FamilyInfoHousingResponse.class)
                .optional();

        if (housingData.isPresent())
            return housingData.get();
        else
            return null;
    }

    // معلومات جالة افراد العائلة الصحية
    public FamilyHealthStatusInfoResponse getFamilyHealthStatusInfo(Long P_FAMILY_NO){
        Optional<FamilyHealthStatusInfoResponse> healthData = jdbcClient.sql("""
                        SELECT L1.ARABIC_VALUE AS HouseLegal
                               ,L2.ARABIC_VALUE AS HealthStatus
                               ,L3.ARABIC_VALUE AS IllnessType
                               ,PI.ILLNESS_DESCRIPTION AS IllnessDescription
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                             LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                             LEFT JOIN AIN_CAPPS.SC_CASE_HOUSE_FOLLOW HF ON HF.FOLLOW_ID = D.FOLLOW_ID
                             LEFT JOIN AIN_CAPPS.SC_PERSON_ILLNESS PI ON PI.FAMILY_PERSONS_ID = R.FAMILY_PERSON_ID
                             LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L1 ON L1.LOOKUP_CODE = HF.HOUSE_TYPE_ID AND L1.LOOKUP_TYPE  = 'HOUSE_LEGAL'
                             LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L2 ON L2.LOOKUP_CODE = H.HEALTH_STATUS_ID AND L2.LOOKUP_TYPE  = 'HEALTH_STATUS'
                             LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L3 ON L3.LOOKUP_CODE = PI.ILLNESS_TYPE_ID AND L3.LOOKUP_TYPE  = 'ILLNESS_TYPE'
                        WHERE D.FOLLOW_DESCION_DATE = (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                                                ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                                            LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                                                 ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                                        WHERE R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                                            AND F.OLD_FAMILY_NO = F1.OLD_FAMILY_NO)
                        AND F.OLD_FAMILY_NO = :P_FAMILY_NO
                        AND D.FOLLOW_DESCION_STATUS = 2
                        AND H.RELATION_ID IN (1,5)
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                .query(FamilyHealthStatusInfoResponse.class)
                .optional();

        if (healthData.isPresent())
            return healthData.get();
        else
            return null;
    }
}
