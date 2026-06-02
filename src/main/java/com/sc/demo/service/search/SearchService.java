package com.sc.demo.service.search;

import com.sc.demo.model.dto.search.searchResponse;
import com.sc.demo.model.dto.search.searchResponseV2;
import org.hibernate.type.SqlTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private JdbcClient jdbcClient;

    public List<searchResponse> SearchByName(String GuardianName){
        return jdbcClient.sql("""
                    SELECT * FROM (SELECT   DISTINCT PERSON_NAME_FIRST  || ' '
                                                  || PERSON_NAME_SECOND || ' '
                                                  || PERSON_NAME_THIRD  || ' '
                                                  || PERSON_NAME_FOURTH || ' '
                                                  || ARABIC_VALUE AS GuardianName
                                   ,FAMILY_PERSONS_ID AS FamilyPersonsId
                                   FROM AIN_CAPPS.SC_FAMILY_PERSONS
                                   LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = FAMILY_TITLE_ID AND LOOKUP_TYPE = 'FAMILY_TITLE'
                                   WHERE IS_GUARDIAN = 1
                    ) SearchName
                    WHERE SEARCHNAME.GUARDIANNAME LIKE '%' || :GuardianName || '%'
                """)
                .param("GuardianName", GuardianName, SqlTypes.VARCHAR)
                .query(searchResponse.class)
                .list();
    }


    public List<searchResponseV2> SearchByNameV2(String GuardianName){
        return jdbcClient.sql("""
                    SELECT * FROM (SELECT p.PERSON_NAME_FIRST || ' '
                                         || p.PERSON_NAME_SECOND || ' '
                                         || p.PERSON_NAME_THIRD  || ' '
                                         || p.PERSON_NAME_FOURTH || ' '
                                         || ARABIC_VALUE AS guardianName
                                         ,p1.PERSON_NAME_FIRST || ' '
                                         || p1.PERSON_NAME_SECOND || ' '
                                         || p1.PERSON_NAME_THIRD  || ' '
                                         || p1.PERSON_NAME_FOURTH || ' '
                                         || ARABIC_VALUE AS headFamilyName
                                         ,p.FAMILY_PERSONS_ID AS familyPersonsId
                    FROM AIN_CAPPS.SC_FAMILY_PERSONS p
                    LEFT JOIN AIN_CAPPS.SC_FAMILY_PERSONS p1 on (p1.FAMILY_PERSONS_ID = p.HEAD_FAMILY_ID)
                    LEFT JOIN AIN_CAPPS.FND_LOOKUP_VALUES V ON V.LOOKUP_CODE = p.FAMILY_TITLE_ID AND v.LOOKUP_TYPE = 'FAMILY_TITLE'
                    WHERE p.IS_GUARDIAN = 1
                    ) SearchName
                    WHERE SEARCHNAME.GUARDIANNAME LIKE '%' || :GuardianName || '%'
                """)
                .param("GuardianName", GuardianName, SqlTypes.VARCHAR)
                .query(searchResponseV2.class)
                .list();
    }
}
