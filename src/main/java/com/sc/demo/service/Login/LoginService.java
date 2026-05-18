package com.sc.demo.service.Login;

import com.sc.demo.model.Verification.SendingType;
import com.sc.demo.model.dto.Login.ChekLoginResponse;
import com.sc.demo.model.dto.Login.LogInResponse;
import com.sc.demo.model.Verification.VerificationApp;
import com.sc.demo.repository.VerificationLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LoginService implements CommandLineRunner {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private VerificationLoginRepo verificationLoginRepo;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private TokenService tokenService;

    String regex = "^(77|78|79)\\d{8}$";


    // تسجيل دخول من خلال رقم الهاتف
    public LogInResponse logIn(long phone_Number, String country_code, String birthDate){

        if (!String.valueOf(phone_Number).matches(regex)){
            return null;
        }

        Optional <LogInResponse> logInRes = jdbcClient.sql("""
                        SELECT H.HEAD_FAMILY_ID as HeadFamilyId
                               ,R.AID_REQUEST_ID as RequestId
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
                        AND (F.PHONE1 LIKE '%' || :phone_Number
                            OR F.PHONE2 LIKE '%' || :phone_Number
                            OR F.PHONE3 LIKE '%' || :phone_Number)
                        AND H.BIRTH_DATE = TO_DATE(:birthDate, 'DD/MM/YYYY')

                        UNION

                        SELECT FI.HEAD_FAMILY_ID as HeadFamilyId
                              ,FI.REQUEST_ID as RequestId
                        FROM MOBAPP.SC_FAMILY_INFO FI
                        WHERE FI.PHONE LIKE '%' || :phone_Number
                        AND FI.BIRTH_DATE = TO_DATE(:birthDate, 'DD/MM/YYYY')
                """)
                .param("phone_Number",phone_Number)
                .param("birthDate", birthDate)
                .query(LogInResponse.class)
                .optional();

        if (logInRes.isPresent()) {
            Long code = GeneratingVerificationLogin(phone_Number + country_code, SendingType.WHATSAPP);
            whatsAppService.sendVerificationCode(code);
            return logInRes.get();
        }else
            return null;
    }

    // جلب ال OTP بعد خزنه بالجدول
    public Long GeneratingVerificationLogin(String userIdentifier, SendingType sendingType) {
        Long code;
        code = ThreadLocalRandom.current().nextLong(100000, 1_000_000);
        verificationLoginRepo.save(new VerificationApp(userIdentifier, code, sendingType));
        return code;
    }

    // التحقق من تاريخ الميلاد وارسال رقم الحاتف
    public ChekLoginResponse ChekLoginApp(Long phone_Number, Long secretCode){
        Optional <ChekLoginResponse> logInChek = jdbcClient.sql("""
                        SELECT USER_ID
                        FROM MOBAPP.SC_VERIFICATION_APP V
                        WHERE V.USER_ID LIKE '%' || :phone_Number
                        AND V.SECRET_CODE = :secretCode
                        and Sysdate = CREATE_DATE + interval '10' minute
                """)
                .param("phone_Number",phone_Number)
                .param("secretCode", secretCode)
                .query(ChekLoginResponse.class).optional();

        if (logInChek.isPresent()) {
            return logInChek.get();
        }else
            return null;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(tokenService);
    }
}
