package com.sc.demo.service.users;

import com.sc.demo.model.dto.ChildrenAndMailyFamilyMambersResponse;
import com.sc.demo.model.dto.FamilyHealthStatusInfoResponse;
import com.sc.demo.model.dto.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfoHousingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyInfoService {

    @Autowired
    private JdbcClient jdbcClient;

    // المعلومات العائلة الاساسية
    public List<FamilyInfoBasicResponse> getFamilyBasicInfo(String P_FAMILY_NO){
        return jdbcClient.sql("""
                        SELECT  F.OLD_FAMILY_NO AS FamilyNo
                             ,H.FAMILY_PERSONS_ID AS FamilyPersonsId
                             ,TRIM(
                                  REGEXP_REPLACE(
                                      COALESCE(H.PERSON_NAME_FIRST, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_SECOND, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_THIRD, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_FOURTH, ''),
                                      '\\s+', ' '
                                  )
                              ) AS PersonsFullName
                             ,L3.ARABIC_VALUE AS RelationshipType
                             ,FLOOR(MONTHS_BETWEEN(SYSDATE, H.BIRTH_DATE) / 12) AS PersonsAge
                             ,L2.ARABIC_VALUE AS HealthStatus
                            ,PI.ILLNESS_DESCRIPTION AS HealthConditionDetails
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                             LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                             LEFT JOIN (SELECT ROW_NUMBER() OVER (PARTITION BY FOLLOW_ID ORDER BY HOUSE_FOLLOW_ID DESC) AS SN ,MN.*
                                           FROM AIN_CAPPS.SC_CASE_HOUSE_FOLLOW MN ) HF ON HF.FOLLOW_ID = D.FOLLOW_ID AND HF.SN = 1
                             LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L2 ON L2.LOOKUP_CODE = H.HEALTH_STATUS_ID AND L2.LOOKUP_TYPE  = 'HEALTH_STATUS'
                             LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L3 ON L3.LOOKUP_CODE = H.RELATION_ID AND L3.LOOKUP_TYPE  = 'SC_RELETIVES_TYPIES'
                             LEFT JOIN (SELECT FAMILY_PERSONS_ID ,LISTAGG(ILLNESS_DESCRIPTION, ', ') WITHIN GROUP (ORDER BY ILLNESS_DESCRIPTION) AS ILLNESS_DESCRIPTION
                                          FROM AIN_CAPPS.SC_PERSON_ILLNESS
                                          GROUP BY FAMILY_PERSONS_ID)PI  ON H.FAMILY_PERSONS_ID = PI.FAMILY_PERSONS_ID
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
                        AND H.RELATION_ID NOT IN (7,8)
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                    .query(FamilyInfoBasicResponse.class)
                    .list();
    }

    // ارقام الهواتف + سكن العائلة + فرع التسجيل
    public List<FamilyInfoHousingResponse> getFamilyHousingInfo(String P_FAMILY_NO){
        return jdbcClient.sql("""
                        SELECT G.GOVERNORATE_NAME AS GovernorateName
                                      ,J.JUDGE_NAME AS JudgeName
                                      ,C.CITY_ANAME AS CityName
                                      ,CH.NEIGHBORHOOD AS Neighborhood
                                      ,CH.NEARSET_ADDRESS AS NearsetAddress
                                      ,GL.ARABIC_DESCRIPTION AS BranchName
                                      ,GL2.ARABIC_DESCRIPTION AS OfficeName
                                      ,R.PHONE_1 AS Phone1
                                      ,R.PHONE_2 AS Phone2
                                      ,R.PHONE_3 AS Phone3
                                      ,L1.ARABIC_VALUE AS HousingType
                                FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                                      LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON D.FOLLOW_ID = F.FOLLOW_ID
                                      LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON F.AID_REQUEST_ID = R.AID_REQUEST_ID
                                      LEFT JOIN AIN_CAPPS.SC_CASE_HOUSE_FOLLOW CH ON CH.FOLLOW_ID = D.FOLLOW_ID
                                      LEFT JOIN AIN_CAPPS.FND_GOVERNORATES G ON G.GOVERNORATE_ID = CH.GOVERNEMENT_ID
                                      LEFT JOIN AIN_CAPPS.FND_JUDGES J ON J.JUDGE_ID = CH.JUDGE_ID
                                      LEFT JOIN AIN_CAPPS.FND_CITIES C ON C.CITY_ID = CH.CITY_ID
                                      LEFT JOIN AIN_CAPPS.GL_ACC_DETAILS GL ON GL.VALUE_CODE = F.ORG_ID and GL.SEGMENT_ID = 1
                                      LEFT JOIN AIN_CAPPS.GL_ACC_DETAILS GL2 ON GL2.VALUE_CODE = F.ORG_OFFICE_ID and GL2.SEGMENT_ID = 10
                                        LEFT JOIN (SELECT ROW_NUMBER() OVER (PARTITION BY FOLLOW_ID ORDER BY HOUSE_FOLLOW_ID DESC) AS SN ,MN.*
                                                      FROM AIN_CAPPS.SC_CASE_HOUSE_FOLLOW MN ) HF ON HF.FOLLOW_ID = D.FOLLOW_ID AND HF.SN = 1
                                      LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L1 ON L1.LOOKUP_CODE = HF.HOUSE_TYPE_ID AND L1.LOOKUP_TYPE  = 'HOUSE_LEGAL'
                                WHERE D.FOLLOW_DESCION_DATE = (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                                             FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                                                  LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                                                     ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                                                 LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                                                      ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                                             WHERE R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                                                 AND F.OLD_FAMILY_NO = F1.OLD_FAMILY_NO)
                                AND F.OLD_FAMILY_NO = :P_FAMILY_NO
                                AND D.FOLLOW_DESCION_STATUS = '2'
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                .query(FamilyInfoHousingResponse.class)
                .list();
    }

    // عدد افراد العائلة + عدد الايتام
    public List<ChildrenAndMailyFamilyMambersResponse> getChildrenAndMailyFamilyMambersResponse(String P_FAMILY_NO){
        return jdbcClient.sql("""
                        SELECT SUM (CASE WHEN H1.RELATION_ID NOT IN (7, 8, 100) THEN 1 END) MailyMambers,
                                       SUM (CASE WHEN H1.RELATION_ID IN (1, 5) THEN 1 END) FamilyChildren
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                               LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                   ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                               LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                   ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                               LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1
                                   ON (H1.FOLLOW_ID = F1.FOLLOW_ID)
                        WHERE D1.FOLLOW_DESCION_DATE = (SELECT MAX (D2.FOLLOW_DESCION_DATE)
                                                         FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D2
                                                               LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F2
                                                                   ON (D2.FOLLOW_ID = F2.FOLLOW_ID)
                                                               LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R2
                                                                   ON (F2.AID_REQUEST_ID = R2.AID_REQUEST_ID)
                                                         WHERE R1.FAMILY_PERSON_ID = R2.FAMILY_PERSON_ID
                                                         AND F1.OLD_FAMILY_NO = F2.OLD_FAMILY_NO)
                        AND F1.OLD_FAMILY_NO = :P_FAMILY_NO
                        AND D1.FOLLOW_DESCION_STATUS = '2'
                """).param("P_FAMILY_NO", P_FAMILY_NO)
                .query(ChildrenAndMailyFamilyMambersResponse.class)
                .list();
    }
}
