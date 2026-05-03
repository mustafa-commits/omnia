package com.sc.demo.service.inquiry;

import com.sc.demo.model.dto.InquiryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {

    @Autowired
    private JdbcClient jdbcClient;

    // استعلام بأسم الوصي
    public List<InquiryRequest> inquiryByName(String GuardianName){
        return jdbcClient.sql("""
                SELECT (H.PERSON_NAME_FIRST || ' ' ||
                       H.PERSON_NAME_SECOND || ' ' ||
                       H.PERSON_NAME_THIRD || ' ' ||
                       H.PERSON_NAME_FOURTH) AS GuardianFullName
                       ,(SELECT H1.PERSON_NAME_FIRST || ' ' || H1.PERSON_NAME_SECOND || ' ' || H1.PERSON_NAME_THIRD || ' ' || H1.PERSON_NAME_FOURTH AS PersonsFullName
                            FROM AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1
                            JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1 ON (H1.FOLLOW_ID = F1.FOLLOW_ID)
                            WHERE H1.HEAD_FAMILY_ID IS NULL
                            and F1.OLD_FAMILY_NO = F.OLD_FAMILY_NO
                            order by 1 desc fetch first row only) AS HeadFamilyFullName
                FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                    LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                    LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                WHERE (H.PERSON_NAME_FIRST || ' ' ||
                       H.PERSON_NAME_SECOND || ' ' ||
                       H.PERSON_NAME_THIRD || ' ' ||
                       H.PERSON_NAME_FOURTH) LIKE '%' || :GuardianName || '%'
                AND D.CREATION_DATE = (
                    SELECT MAX(D2.CREATION_DATE)
                    FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D2
                    JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F2 ON (D2.FOLLOW_ID = F2.FOLLOW_ID)
                    WHERE F2.OLD_FAMILY_NO = F.OLD_FAMILY_NO)
                """)
                .param("GuardianName", GuardianName)
                .query(InquiryRequest.class)
                .list();
    }
}
