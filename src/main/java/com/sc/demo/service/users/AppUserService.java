package com.sc.demo.service.users;

import com.sc.demo.model.dto.AppUserRequest;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private JdbcClient jdbcClient;

    public AppUserRequest getFamilyInfoInHomePage(Long P_FAMILY_ID){
        Optional<AppUserRequest> userData = jdbcClient.sql("""
                        SELECT R.FAMILY_PERSON_ID AS FamilyPersonId
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
                              ,H.BIRTH_DATE AS BirthDate
                              ,H.GENDER AS Gender
                              ,R.PHONE_1 AS Phone1
                              ,R.PHONE_2 AS Phone2
                              ,R.PHONE_3 AS Phone3
                              ,(SELECT  COUNT(*)\s
                                       FROM AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1\s
                                       Where H1.FAMILY_PERSONS_ID = H.FAMILY_PERSONS_ID) as NoOfMambers
                              ,(SELECT  COUNT(*)\s
                                       FROM AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1\s
                                       Where H1.FAMILY_PERSONS_ID = H.FAMILY_PERSONS_ID
                                       AND H1.RELATION_ID IN (2,5)) as FamilyNumberChildren
                              ,MIN(CS.STARTDATE) AS Startdate
                              ,R.NEIGHBORHOOD AS Neighborhood
                              ,R.NEARSET_PLACE AS NearsetPlace
                              ,HF.HOUSE_TYPE_ID AS HouseTypeId
                              ,R.PLACE_OF_REQUEST AS PlaceOfRequest
                              ,FLOOR(MONTHS_BETWEEN(SYSDATE, H.BIRTH_DATE) / 12) AS Age
                              ,L.ARABIC_VALUE AS AcademicAchievement
                              ,L1.ARABIC_VALUE AS HouseLegal
                              ,L2.ARABIC_VALUE AS HealthStatus
                              ,L3.ARABIC_VALUE AS IllnessType
                              ,PI.ILLNESS_DESCRIPTION AS IllnessDescription
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                              LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                              LEFT JOIN AIN_CAPPS.SP_CONTRACT_CASES CS ON CS.FAMILY_PERSON_ID = H.FAMILY_PERSONS_ID
                              LEFT JOIN AIN_CAPPS.SP_CONTRACT C ON C.CONTRACT_ID = CS.CONTRACT_ID
                              LEFT JOIN AIN_CAPPS.SC_CASE_HOUSE_FOLLOW HF ON HF.FOLLOW_ID = D.FOLLOW_ID
                              LEFT JOIN AIN_CAPPS.SC_PERSON_ILLNESS PI ON PI.FAMILY_PERSONS_ID = R.FAMILY_PERSON_ID
                              LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L ON L.LOOKUP_CODE = H.ACADEMIC_ACHIEVEMENT_ID AND L.LOOKUP_TYPE  = 'ACADEMIC_ACHIEVEMENT'
                              LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L1 ON L1.LOOKUP_CODE = HF.HOUSE_TYPE_ID AND L1.LOOKUP_TYPE  = 'HOUSE_LEGAL'
                              LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L2 ON L2.LOOKUP_CODE = H.HEALTH_STATUS_ID AND L2.LOOKUP_TYPE  = 'HEALTH_STATUS'
                              LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES L3 ON L3.LOOKUP_CODE = PI.ILLNESS_TYPE_ID AND L3.LOOKUP_TYPE  = 'ILLNESS_TYPE'
                        WHERE D.FOLLOW_DESCION_DATE =
                              (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                 FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                      LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                          ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                      LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                          ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                WHERE     R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                      AND F.OLD_FAM_NO = F1.OLD_FAM_NO)
                       AND D.FOLLOW_DESCION_STATUS =   2
                       AND R.FAMILY_PERSON_ID = :P_FAMILY_ID
                       Group by R.FAMILY_PERSON_ID
                              ,TRIM(
                                   REGEXP_REPLACE(
                                       COALESCE(H.PERSON_NAME_FIRST, '') || ' ' ||
                                       COALESCE(H.PERSON_NAME_SECOND, '') || ' ' ||
                                       COALESCE(H.PERSON_NAME_THIRD, '') || ' ' ||
                                       COALESCE(H.PERSON_NAME_FOURTH, ''),
                                       '\\s+', ' '
                                   )
                               )
                              ,H.FAMILY_PERSONS_ID
                              ,H.RELATION_ID
                              ,H.BIRTH_DATE
                              ,H.GENDER
                              ,R.PHONE_1
                              ,R.PHONE_2
                              ,R.PHONE_3
                              ,R.NEIGHBORHOOD
                              ,R.NEARSET_PLACE
                              ,HF.HOUSE_TYPE_ID
                              ,R.PLACE_OF_REQUEST
                              ,L.ARABIC_VALUE
                              ,L1.ARABIC_VALUE
                              ,L2.ARABIC_VALUE\s
                              ,L3.ARABIC_VALUE
                              ,PI.ILLNESS_DESCRIPTION
                       order by R.FAMILY_PERSON_ID
                """).param("P_FAMILY_ID", P_FAMILY_ID)
                //AND H.IS_GUARDIAN = :P0_1
                //AND     H.RELATION_ID != 100
                    .query(AppUserRequest.class)
                    .optional();

        if (userData.isPresent())
            return userData.get();
        else
            return null;
    }
}
