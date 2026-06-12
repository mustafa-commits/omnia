package com.sc.demo.service.addPhone;

import com.sc.demo.model.dto.addPhoneNumber.AddPhonRequest;
import com.sc.demo.model.dto.addPhoneNumber.CheckPhoneRequest;
import com.sc.demo.model.familyInfo.FamilyInfo;
import com.sc.demo.repository.addPhone.AddPhoneRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddPhoneService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AddPhoneRepo addPhoneRepo;

    public CheckPhoneRequest checkForTheNumber(long phone){
        Optional <CheckPhoneRequest> check = jdbcClient.sql("""
                        SELECT P.PERSON_NAME_FIRST || ' '
                             || P.PERSON_NAME_SECOND || ' '
                             || P.PERSON_NAME_THIRD  || ' '
                             || P.PERSON_NAME_FOURTH || ' '
                             || ARABIC_VALUE AS headFamilyName
                             ,P.BIRTH_DATE AS birthDate
                             ,R.AID_REQUEST_ID AS requestId
                             ,p.FAMILY_PERSONS_ID AS headFamilyId
                             ,F.ORG_ID AS branches
                             ,F.OLD_FAMILY_NO AS oldFamilyNo
                        FROM AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F
                        LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                        Left Join AIN_CAPPS.SC_FAMILY_PERSONS p on (R.FAMILY_PERSON_ID = p.FAMILY_PERSONS_ID)
                        LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = p.FAMILY_TITLE_ID AND v.LOOKUP_TYPE = 'FAMILY_TITLE'
                        WHERE (F.PHONE1 LIKE '%' || :phone
                             OR F.PHONE2 LIKE '%' || :phone
                             OR F.PHONE3 LIKE '%' || :phone)
    
                        UNION
    
                        SELECT HEAD_FAMILY_NAME AS headFamilyName
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
                .optional();


        if (check.isPresent()) {
            return check.get();
        }else
            return null;

    }

    public Boolean addPhone(AddPhonRequest addPhonRequest){
        Optional<FamilyInfo> byHeadAndRequestId = addPhoneRepo.findById(addPhonRequest.headFamilyId());

        addPhoneRepo.save(new FamilyInfo(addPhonRequest.headFamilyId(), addPhonRequest.requestId(), addPhonRequest.headFamilyName(),
                addPhonRequest.createBy(), addPhonRequest.birthDate(), addPhonRequest.phone(), addPhonRequest.oldFamilyNo(), addPhonRequest.branches()));

        return true;
    }
}
