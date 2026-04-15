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

    public AppUserRequest getFamilyInfoInHomePage(AppUserRequest appUserRequest){
        Optional<AppUserRequest> userData = jdbcClient.sql("""
                        SELECT R.FAMILY_PERSON_ID
                               ,TRIM(
                                  REGEXP_REPLACE(
                                      COALESCE(H.PERSON_NAME_FIRST, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_SECOND, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_THIRD, '') || ' ' ||
                                      COALESCE(H.PERSON_NAME_FOURTH, ''),
                                      '\\s+', ' '
                                  )
                              ) AS PERSON_FULL_NAME
                              ,H.FAMILY_PERSONS_ID
                              ,H.RELATION_ID
                              ,H.BIRTH_DATE
                              ,H.GENDER
                              ,R.PHONE_1
                              ,R.PHONE_2
                              ,R.PHONE_3
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                             LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID
                             )
                        WHERE D.FOLLOW_DESCION_DATE =
                             (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                     LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                         ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                     LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                         ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                               WHERE     R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                     AND F.OLD_FAM_NO = F1.OLD_FAM_NO)
                        AND D.FOLLOW_DESCION_STATUS = 2
                        order by R.FAMILY_PERSON_ID;
                """).param("user_id", appUserRequest.user_id())
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
