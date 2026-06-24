package com.sc.demo.service.address;

import com.sc.demo.model.dto.addresses.Branches;
import com.sc.demo.model.dto.addresses.Departments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private JdbcClient jdbcClient;

    public List<Branches> getBranches(){
        return jdbcClient.sql("""
                SELECT GL_BRAN.VALUE_CODE AS code
                      ,GL_BRAN.ARABIC_DESCRIPTION AS name
                FROM AIN_CAPPS.GL_ACC_DETAILS GL_BRAN
                WHERE GL_BRAN.SEGMENT_ID = 1
                """)
                .query(Branches.class)
                .list();
    }

    public List<Departments> getDepartments(){
        return jdbcClient.sql("""
                SELECT ORGANIZATION_ID AS departmentId,
                       ORGANIZATION_NAME AS departmentName
                FROM AIN_CAPPS.HR_ORGANIZATIONS_D
                WHERE PARENT_ORGANIZATION_ID = 12
                ORDER BY ORGANIZATION_ID
                """)
                .query(Departments.class)
                .list();
    }
}
