package com.sc.demo.service.addPhone;

import com.sc.demo.model.dto.addPhoneNumber.AddPhonRequest;
import com.sc.demo.model.dto.addPhoneNumber.AllPhones;
import com.sc.demo.model.dto.addPhoneNumber.CheckPhoneRequest;
import com.sc.demo.model.familyInfo.FamilyInfo;
import com.sc.demo.repository.addPhone.AddPhoneRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddPhoneService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AddPhoneRepo addPhoneRepo;

    @Autowired
    private TokenService tokenService;

    public List<CheckPhoneRequest> checkForTheNumber(long phone){
        return jdbcClient.sql("""
              SELECT  H.PERSON_NAME_FIRST || ' ' ||
                      H.PERSON_NAME_SECOND || ' ' ||
                      H.PERSON_NAME_THIRD  || ' ' ||
                      H.PERSON_NAME_FOURTH || ' ' ||
                      ARABIC_VALUE AS headFamilyName
                     ,H1.PERSON_NAME_FIRST || ' ' ||
                      H1.PERSON_NAME_SECOND || ' ' ||
                      H1.PERSON_NAME_THIRD || ' ' ||
                      H1.PERSON_NAME_FOURTH AS guardianName
                     ,H1.BIRTH_DATE AS birthDate
                     ,R.AID_REQUEST_ID AS requestId
                     ,H1.HEAD_FAMILY_ID AS headFamilyId
                     ,F.ORG_ID AS branches
                     ,F.OLD_FAMILY_NO AS oldFamilyNo
              FROM  AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
              LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
              LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (R.FAMILY_PERSON_ID = H.FAMILY_PERSONS_ID)
              LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1 ON (R.FAMILY_PERSON_ID = H1.HEAD_FAMILY_ID AND H1.IS_GUARDIAN = 1)
              LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = H.FAMILY_TITLE_ID AND V.LOOKUP_TYPE = 'FAMILY_TITLE'
              WHERE (F.PHONE1 LIKE '%' || :phone
                 OR F.PHONE2 LIKE '%' || :phone
                 OR F.PHONE3 LIKE '%' || :phone)
              AND D.FOLLOW_DESCION_DATE = (SELECT MAX (D1.FOLLOW_DESCION_DATE)
                                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D1
                                             LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1
                                               ON (D1.FOLLOW_ID = F1.FOLLOW_ID)
                                            LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R1
                                                 ON (F1.AID_REQUEST_ID = R1.AID_REQUEST_ID)
                                        WHERE R.FAMILY_PERSON_ID = R1.FAMILY_PERSON_ID
                                            AND F.OLD_FAMILY_NO = F1.OLD_FAMILY_NO)
               UNION ALL

               SELECT HEAD_FAMILY_NAME AS headFamilyName
                     ,GUARDIAN_NAME AS guardianName
                    ,BIRTH_DATE AS birthDate
                     ,REQUEST_ID AS requestId
                     ,HEAD_FAMILY_ID AS headFamilyId
                     ,BRANCHES AS branches
                     ,OLD_FAMILY_NO AS oldFamilyNo
               FROM MOBAPP.SC_FAMILY_INFO FI
               WHERE FI.PHONE LIKE '%' || :phone
               """)
                .param("phone", phone)
                .query(CheckPhoneRequest.class)
                .list();
    }

    // اضافة رقم هاتف
    public boolean addPhone(AddPhonRequest addPhonRequest, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();
        List<Long> addingPhone = jdbcClient.sql("""
                        SELECT INFO_ID
                        FROM MOBAPP.SC_FAMILY_INFO
                        WHERE PHONE = :phone
                        AND HEAD_FAMILY_ID = :headFamilyId
                        AND REQUEST_ID = :requestId
                        """)
                .param("phone", addPhonRequest.phone())
                .param("headFamilyId", addPhonRequest.headFamilyId())
                .param("requestId", addPhonRequest.requestId())
                .query(Long.class)
                .list();

        if (addingPhone.isEmpty()){
            addPhoneRepo.save(new FamilyInfo(addPhonRequest.guardianName(), addPhonRequest.headFamilyId(), addPhonRequest.requestId(),
                addPhonRequest.headFamilyName(), Long.parseLong(userDashboardId),
                addPhonRequest.birthDate(), addPhonRequest.phone(),
                addPhonRequest.oldFamilyNo(), addPhonRequest.branches()));
            return true;
        }
            return false;
    }

    public List<AllPhones> allNewPhone(){
        return jdbcClient.sql("""
                SELECT F.INFO_ID AS infoId,
                       F.PHONE AS phone,
                       F.BIRTH_DATE AS birthDate,
                       DU.FULL_NAME AS createBy,
                       F.CREATE_DATE AS createDate,
                       F.HEAD_FAMILY_NAME AS headFamilyName,
                       F.OLD_FAMILY_NO AS oldFamilyNo,
                       F.GUARDIAN_NAME AS guardianName
                FROM MOBAPP.SC_FAMILY_INFO F
                LEFT JOIN MOBAPP.SC_DASHBOARD_USER DU ON F.CREATE_BY = DU.USER_ID
                ORDER BY F.CREATE_DATE DESC
                """)
                .query(AllPhones.class)
                .list();
    }
}
