package com.sc.demo.service.address;

import com.sc.demo.model.dto.branches.Branches;
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
                """)
                .query(Branches.class)
                .list();
    }
}
