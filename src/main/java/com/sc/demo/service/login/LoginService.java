package com.sc.demo.service.login;

import com.sc.demo.model.dto.familyInfo.AppUserRequest;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.verification.SendingType;
import com.sc.demo.model.dto.login.ChekLoginRequest;
import com.sc.demo.model.dto.login.LogInResponse;
import com.sc.demo.model.verification.VerificationApp;
import com.sc.demo.repository.login.AppUserRepo;
import com.sc.demo.repository.login.VerificationLoginRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private AppUserRepo appUserRepo;

    String regex = "^(77|78|79)\\d{8}$";

    // تسجيل دخول من خلال رقم الهاتف
    public List<LogInResponse> logIn(long phone, String country_code, String birthDate){

        if (!String.valueOf(phone).matches(regex)){
            return null;
        }

        Long code = GeneratingVerificationLogin(String.valueOf(phone), SendingType.WHATSAPP);
        whatsAppService.sendVerificationCode(code);

        return jdbcClient.sql("""
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
                        AND (F.PHONE1 LIKE '%' || :phone
                            OR F.PHONE2 LIKE '%' || :phone
                            OR F.PHONE3 LIKE '%' || :phone)
                        AND H.BIRTH_DATE = TO_DATE(:birthDate, 'DD/MM/YYYY')

                        UNION

                        SELECT FI.HEAD_FAMILY_ID as HeadFamilyId
                              ,FI.REQUEST_ID as RequestId
                        FROM MOBAPP.SC_FAMILY_INFO FI
                        WHERE FI.PHONE LIKE '%' || :phone
                        AND FI.BIRTH_DATE = TO_DATE(:birthDate, 'DD/MM/YYYY')
                """)
                .param("phone",phone)
                .param("birthDate", birthDate)
                .query(LogInResponse.class)
                .list();
    }

    // جلب ال OTP بعد خزنه بالجدول
    public Long GeneratingVerificationLogin(String userIdentifier, SendingType sendingType) {
        Long code;
        code = ThreadLocalRandom.current().nextLong(100000, 1_000_000);
        verificationLoginRepo.save(new VerificationApp(userIdentifier, code, sendingType));
        return code;
    }

    // التحقق من تاريخ الميلاد وارسال رقم الحاتف
    public ResponseEntity<?> ChekLoginApp(AppUserRequest appUserRequest){
        Optional <ChekLoginRequest> logInChek = jdbcClient.sql("""
                    SELECT USER_IDENTIFIER AS userIdentifier
                    FROM MOBAPP.SC_VERIFICATION_APP V
                    WHERE V.USER_IDENTIFIER = :phone
                    AND V.SECRET_CODE = :secretCode
                    and Sysdate <= CREATE_DATE + interval '10' minute
                """)
                .param("phone",appUserRequest.phone())
                .param("secretCode", appUserRequest.secretCode())
                .query(ChekLoginRequest.class).optional();

        if (logInChek.isPresent()) {
            List<getUserIdWithToken> setGuardianInfo = new ArrayList<>();
            List<LogInResponse> responseList = jdbcClient.sql("""
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
                        AND (F.PHONE1 LIKE '%' || :phone
                            OR F.PHONE2 LIKE '%' || :phone
                            OR F.PHONE3 LIKE '%' || :phone)

                        UNION

                        SELECT FI.HEAD_FAMILY_ID as HeadFamilyId
                              ,FI.REQUEST_ID as RequestId
                        FROM MOBAPP.SC_FAMILY_INFO FI
                        WHERE FI.PHONE LIKE '%' || :phone
                        """)
                    .param("phone", appUserRequest.phone())
                    .query(LogInResponse.class)
                    .list();

            // بعد التأكد من رقم الهاتف تضيف المعلومات في AppUsers
            for (LogInResponse response : responseList){
                boolean alreadyExists = appUserRepo.existsByHeadFamilyIdAndRequestId(
                        response.HeadFamilyId(),
                        response.RequestId()
                );

                Long userId;

                if (!alreadyExists) {
                    userId = appUserRepo.save(new AppUser(appUserRequest.phone(), response.RequestId(), response.HeadFamilyId())).getUserid();
                }else {
                    userId = appUserRepo.findByHeadFamilyIdAndRequestId(
                            response.HeadFamilyId(),
                            response.RequestId()
                    ).getUserid();
                }
                setGuardianInfo.add(new getUserIdWithToken(userId, tokenService.generateToken(String.valueOf(userId),response.RequestId(), response.HeadFamilyId())));
            }
           return ResponseEntity.ok(setGuardianInfo);
        }else
            return ResponseEntity.badRequest().body("WRONG CRED");
    }

    public record getUserIdWithToken (Long userId, String token){

    }

    @Override
    public void run(String... args) throws Exception {

//        var userId = tokenService.decodeToken(token.substring(7)).getSubject();
//        Long headFamilyId = tokenService.decodeToken(token.substring(7)).getClaim("headFamilyId");
//        Long requestId = tokenService.decodeToken(token.substring(7)).getClaim("requestId");
//
        System.out.println(tokenService.generateToken("77", 73619L, 506173L));
    }
}
