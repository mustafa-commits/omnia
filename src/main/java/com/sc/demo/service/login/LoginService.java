package com.sc.demo.service.login;

import com.sc.demo.model.verification.sendingType;
import com.sc.demo.model.dto.login.chekLoginRequest;
import com.sc.demo.model.dto.login.logInResponse;
import com.sc.demo.model.verification.verificationApp;
import com.sc.demo.repository.login.VerificationLoginRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
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
    public logInResponse logIn(long phone_Number, String country_code, String birthDate){

        if (!String.valueOf(phone_Number).matches(regex)){
            return null;
        }

        Optional <logInResponse> logInRes = jdbcClient.sql("""
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
                .query(logInResponse.class)
                .optional();

        if (logInRes.isPresent()) {
            Long code = GeneratingVerificationLogin(String.valueOf(phone_Number), sendingType.WHATSAPP);
            whatsAppService.sendVerificationCode(code);
            return logInRes.get();
        }else
            return null;
    }

    // جلب ال OTP بعد خزنه بالجدول
    public Long GeneratingVerificationLogin(String userIdentifier, sendingType sendingType) {
        Long code;
        code = ThreadLocalRandom.current().nextLong(100000, 1_000_000);
        verificationLoginRepo.save(new verificationApp(userIdentifier, code, sendingType));
        return code;
    }

    // التحقق من تاريخ الميلاد وارسال رقم الحاتف
    public ResponseEntity<?> ChekLoginApp(Long phone_Number, Long secretCode){
        Optional <chekLoginRequest> logInChek = jdbcClient.sql("""
                        SELECT USER_IDENTIFIER AS userIdentifier
                        FROM MOBAPP.SC_VERIFICATION_APP V
                        WHERE V.USER_IDENTIFIER = :phone_Number
                        AND V.SECRET_CODE = :secretCode
                        and Sysdate <= CREATE_DATE + interval '10' minute
                """)
                .param("phone_Number",phone_Number)
                .param("secretCode", secretCode)
                .query(chekLoginRequest.class).optional();

        if (logInChek.isPresent()) {
            return ResponseEntity.ok(tokenService.generateToken("1"));

        }else
            return ResponseEntity.badRequest().body("WRONG CRED");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(tokenService.generateToken("1"));
    }
}
