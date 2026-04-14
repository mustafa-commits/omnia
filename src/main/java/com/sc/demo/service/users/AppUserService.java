package com.sc.demo.service.users;

import com.sc.demo.model.dto.AppUserRequest;
import com.sc.demo.model.users.AppUser;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private JdbcClient jdbcClient;

    public AppUser getFamilyInfoInHomePage(AppUserRequest appUserRequest){
        Optional<AppUserRequest> userData = jdbcClient.sql("""
                        SELECT * FROM DUAL;
                """).param("id", appUserRequest.id())
                    .query(AppUserRequest.class)
                    .optional();

        if (userData.isPresent())
            return userData.get();
        else
            return null;
    }
}
