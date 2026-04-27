package com.sc.demo.service.users;

import com.sc.demo.model.dto.LogInResponse1;
import com.sc.demo.model.dto.VerificationLoginResponse;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.dto.AppUserRequest2;
import com.sc.demo.model.dto.LoginResponse;
import com.sc.demo.model.users.VerificationApp;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LoginService {

    @Autowired
    private JdbcClient jdbcClient;

    // تسجيل دخول من خلال رقم الهاتف
    public LogInResponse1 logIn(String P_Phone_Number){

        Optional <LogInResponse1> logInRes = jdbcClient.sql("""
                   SELECT H.FAMILY_PERSONS_ID as UserId
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
                   AND H.IS_GUARDIAN = 1
                   AND (F.PHONE1 LIKE '%' || :P_Phone_Number
                   OR F.PHONE2 LIKE '%' || :P_Phone_Number
                   OR F.PHONE3 LIKE '%' || :P_Phone_Number)

                   UNION

                   SELECT FI.USER_ID as UserId
                   FROM MOBAPP.SC_FAMILY_INFO FI
                   WHERE FI.PHONE LIKE '%' || :P_Phone_Number
                """).param("P_Phone_Number",P_Phone_Number).query(LogInResponse1.class).optional();

        if (logInRes.isPresent())
            return logInRes.get();
        else
            return null;
    }

    // جلب ال OTP بعد خزنه بالجدول
//    public Long VerificationLoginApp(Long UserId, Integer sendingType, String Mobile) {
//        Long code;
//        code = ThreadLocalRandom.current().nextLong(100000, 1_000_000);
//        VerificationLoginResponse.save(new VerificationApp(UserId, code, sendingType, Mobile));
//        return code;
//    }

}
