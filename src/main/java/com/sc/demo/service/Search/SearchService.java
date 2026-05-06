package com.sc.demo.service.Search;

import com.sc.demo.model.dto.Search.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private JdbcClient jdbcClient;

    // استعلام بأسم الوصي او رمز الوصي
    public List<SearchRequest> SearchByNameOrId(String GuardianName, long GuardianId){
        return jdbcClient.sql("""
                    SELECT H.FAMILY_PERSONS_ID
                           ,(H.PERSON_NAME_FIRST || ' ' ||
                           H.PERSON_NAME_SECOND || ' ' ||
                           H.PERSON_NAME_THIRD || ' ' ||
                           H.PERSON_NAME_FOURTH) AS GuardianFullName
                           ,(SELECT H1.PERSON_NAME_FIRST || ' ' || H1.PERSON_NAME_SECOND || ' ' || H1.PERSON_NAME_THIRD || ' ' || H1.PERSON_NAME_FOURTH AS PersonsFullName
                                FROM AIN_CAPPS.SC_FAMILY_PERSONS_HIST H1
                                JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F1 ON (H1.FOLLOW_ID = F1.FOLLOW_ID)
                                JOIN AIN_CAPPS.SC_AID_REQUESTS R1 ON F1.AID_REQUEST_ID = R1.AID_REQUEST_ID
                                WHERE H1.HEAD_FAMILY_ID IS NULL
                                and R1.WIFE_ID = R.WIFE_ID
                                order by 1 desc fetch first row only) AS HeadFamilyFullName
                    FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D
                        LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F ON (D.FOLLOW_ID = F.FOLLOW_ID)
                        LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS_HIST H ON (H.FOLLOW_ID = F.FOLLOW_ID)
                        LEFT JOIN AIN_CAPPS.SC_AID_REQUESTS R ON F.AID_REQUEST_ID = R.AID_REQUEST_ID
                    WHERE (((H.PERSON_NAME_FIRST || ' ' ||
                           H.PERSON_NAME_SECOND || ' ' ||
                           H.PERSON_NAME_THIRD || ' ' ||
                           H.PERSON_NAME_FOURTH) LIKE '%' || :GuardianName || '%' AND :GuardianName IS NOT NULL )
                           OR H.FAMILY_PERSONS_ID = :GuardianId)
                    AND D.CREATION_DATE = (
                        SELECT MAX(D2.CREATION_DATE)
                        FROM AIN_CAPPS.SC_AID_FOLLOW_DESCION_HD  D2
                        JOIN AIN_CAPPS.SC_AID_REQUESTS_FOLLOW F2 ON (D2.FOLLOW_ID = F2.FOLLOW_ID)
                        JOIN AIN_CAPPS.SC_AID_REQUESTS R2 ON F2.AID_REQUEST_ID = R2.AID_REQUEST_ID
                        WHERE R2.WIFE_ID = R.WIFE_ID)
                """)
                .param("GuardianName", GuardianName)
                .param("GuardianId", GuardianId)
                .query(SearchRequest.class)
                .list();
    }
}
