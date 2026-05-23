package com.sc.demo.service.AddPhone;

import com.sc.demo.model.dto.AddPhone.CheckPhone;
import com.sc.demo.model.verification.SendingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddPhoneService {

    @Autowired
    private JdbcClient jdbcClient;

    public CheckPhone checkForTheNumber(long phone_Number){
        Optional <CheckPhone> check = jdbcClient.sql("""
                        SELECT p.PERSON_NAME_FIRST || ' '
                                                     || p.PERSON_NAME_SECOND || ' '
                                                     || p.PERSON_NAME_THIRD  || ' '
                                                     || p.PERSON_NAME_FOURTH || ' '
                                                     || ARABIC_VALUE AS headFamilyName
                                FROM AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F
                                LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON (F.AID_REQUEST_ID = R.AID_REQUEST_ID)
                                Left Join AIN_CAPPS.SC_FAMILY_PERSONS p on (R.FAMILY_PERSON_ID = p.FAMILY_PERSONS_ID)
                                LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = p.FAMILY_TITLE_ID AND v.LOOKUP_TYPE = 'FAMILY_TITLE'
                                WHERE (F.PHONE1 LIKE '%' || :phone_Number
                                    OR F.PHONE2 LIKE '%' || :phone_Number
                                    OR F.PHONE3 LIKE '%' || :phone_Number)
                        
                                UNION
                        
                                SELECT HEAD_FAMILY_NAME AS headFamilyName
                                FROM MOBAPP.SC_FAMILY_INFO FI
                                WHERE FI.PHONE LIKE '%' || :phone_Number
                """)
                .param("phone_Number", phone_Number)
                .query(CheckPhone.class)
                .optional();


        if (check.isPresent()) {
            return check.get();
        }else
            return null;

    }

//    public String addPhone(){
//
//    }
}
