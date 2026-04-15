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
                        SELECT R.FAMILY_PERSON_ID,H.FAMILY_PERSONS_ID,H.RELATION_ID
                                  FROM SC_AID_FOLLOW_DESCION_HD  D
                                       LEFT JOIN SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                                       LEFT JOIN SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                                       LEFT JOIN SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID AND H.IS_GUARDIAN = :P0_1)
                                WHERE D.FOLLOW_DESCION_DATE =
                                       (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                          FROM SC_AID_FOLLOW_DESCION_HD  D1
                                               LEFT JOIN SC_AID_REQUESTS_FOLLOW F1
                                                   ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                               LEFT JOIN SC_AID_REQUESTS R1
                                                   ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                         WHERE     R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                               AND F.OLD_FAM_NO = F1.OLD_FAM_NO)
                                AND D.FOLLOW_DESCION_STATUS =   2
                """).param("user_id", appUserRequest.user_id())
                    .query(AppUserRequest.class)
                    .optional();

        if (userData.isPresent())
            return userData.get();
        else
            return null;
    }
}
