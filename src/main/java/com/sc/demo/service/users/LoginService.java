package com.sc.demo.service.users;

import com.sc.demo.model.dto.LogInResponse1;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.dto.AppUserRequest2;
import com.sc.demo.model.dto.LoginResponse;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AppUserRepo appUserRepo;


    @Autowired
    private JdbcClient jdbcClient;

    // تسجيل دخول الوصي من خلال رقم الهاتف
    public LogInResponse1 logIn(String P_Phone_Number){

        Optional <LogInResponse1> logInRes = jdbcClient.sql("""
                   SELECT H.FAMILY_PERSONS_ID as UserId
                         ,TRIM(
                            REGEXP_REPLACE(
                                COALESCE(H.PERSON_NAME_FIRST, '') || ' ' ||
                                COALESCE(H.PERSON_NAME_SECOND, '') || ' ' ||
                                COALESCE(H.PERSON_NAME_THIRD, '') || ' ' ||
                                COALESCE(H.PERSON_NAME_FOURTH, ''),
                                '\\s+', ' '
                            )
                         ) as HeadFamilyName
                         ,F.OLD_FAMILY_NO as FamilyNo
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
                        ,FI.HEAD_FAMILY_NAME as HeadFamilyName
                        ,FI.OLD_FAMILY_No as FamilyNo
                   FROM MOBAPP.SC_FAMILY_INFO FI
                   WHERE FI.PHONE LIKE '%' || :P_Phone_Number
                """).param("P_Phone_Number",P_Phone_Number).query(LogInResponse1.class).optional();

        if (logInRes.isPresent())
            return logInRes.get();
        else
            return null;
    }

//    public AppUser login(long id) {
//        Optional<AppUser> byId = appUserRepo.findById(id);
//
//        if (byId.isPresent()) {
//            return byId.get();
//        } else
//            return null;
//
//    }
//
//
//    public AppUser login2(long id) {
//
//        AppUser data = jdbcClient.sql("""
//                    select * from sc_app_user a
//                             left join COUNTRIES c on c.COUNTRY_ID = a.country_id where id = :Id
//                """).param("Id",id).query(AppUser.class).single();
//
//        return data;
//    }
//
//    public LoginResponse login3(long id) {
//
//        LoginResponse data = jdbcClient.sql("""
//                    select a.ID , a.HEAD_FAMILY_NAME, c.NAME countryName from sc_app_user a
//                            left join COUNTRIES c on c.COUNTRY_ID = a.country_id where id = :Id
//                """).param("Id",id).query(LoginResponse.class).single();
//
//        return data;
//    }
//
//    public LoginResponse login4(AppUserRequest2 data) {
//
//
//
//        LoginResponse result = jdbcClient.sql("""
//                    select a.ID , a.HEAD_FAMILY_NAME, c.NAME countryName from sc_app_user a
//                            left join COUNTRIES c on c.COUNTRY_ID = a.country_id where MOBILE1 like :mobile limit 1 -- fetch first record
//                """).param("mobile",data.mobile()).query(LoginResponse.class).single();
//
//        return result;
//
//    }


}
