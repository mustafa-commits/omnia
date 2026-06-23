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

    public List<CheckPhoneRequest> checkForTheNumber(long phone){
        return jdbcClient.sql("""
                SELECT P.PERSON_NAME_FIRST || ' '
                   || P.PERSON_NAME_SECOND || ' '
                   || P.PERSON_NAME_THIRD  || ' '
                   || P.PERSON_NAME_FOURTH || ' '
                   || ARABIC_VALUE AS headFamilyName
                   ,(SELECT P1.PERSON_NAME_FIRST || ' ' ||
                             P1.PERSON_NAME_SECOND || ' ' ||
                             P1.PERSON_NAME_THIRD || ' ' ||
                             P1.PERSON_NAME_FOURTH
                    FROM AIN_CAPPS.SC_FAMILY_PERSONS P1
                    WHERE P.FAMILY_PERSONS_ID = P1.HEAD_FAMILY_ID
                    AND P1.IS_GUARDIAN = 1) AS guardianName
                   ,P.BIRTH_DATE AS birthDate
                   ,R.AID_REQUEST_ID AS requestId
                   ,p.FAMILY_PERSONS_ID AS headFamilyId
                   ,F.ORG_ID AS branches
                   ,F.OLD_FAMILY_NO AS oldFamilyNo
                FROM  AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                LEFT Join AIN_CAPPS.SC_FAMILY_PERSONS P on (R.FAMILY_PERSON_ID = p.FAMILY_PERSONS_ID)
                LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = p.FAMILY_TITLE_ID AND v.LOOKUP_TYPE = FAMILY_TITLE'
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
                UNION

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

    public boolean addPhone(AddPhonRequest addPhonRequest){
        Optional<FamilyInfo> byHeadAndRequestId = addPhoneRepo.findById(addPhonRequest.headFamilyId());
        if (byHeadAndRequestId.isPresent()){
            return false;
        }else {
            addPhoneRepo.save(new FamilyInfo(addPhonRequest.guardianName(), addPhonRequest.headFamilyId(), addPhonRequest.requestId(),
                    addPhonRequest.headFamilyName(), addPhonRequest.createBy(),
                    addPhonRequest.birthDate(), addPhonRequest.phone(),
                    addPhonRequest.oldFamilyNo(), addPhonRequest.branches()));
            return true;
        }
    }

    public List<AllPhones> allNewPhone(){
        return jdbcClient.sql("""
                SELECT INFO_ID AS infoId,
                       PHONE AS phone,
                       BIRTH_DATE AS birthDate,
                       CREATE_BY AS createBy,
                       CREATE_DATE AS createDate,
                       HEAD_FAMILY_NAME AS headFamilyName,
                       OLD_FAMILY_NO AS oldFamilyNo
                FROM MOBAPP.SC_FAMILY_INFO
                ORDER BY CREATE_DATE DESC
                """)
                .query(AllPhones.class)
                .list();
    }
}
