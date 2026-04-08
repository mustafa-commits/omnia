package com.sc.demo.service.users;

import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.dto.AppUserRequest2;
import com.sc.demo.model.dto.LoginResponse;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AppUserRepo appUserRepo;


    @Autowired
    private JdbcClient jdbcClient;


    public AppUser login(long id) {

//        System.out.println(appUserRepo.count());
        Optional<AppUser> byId = appUserRepo.findById(id);

        if (byId.isPresent()) {
            return byId.get();
        } else
            return null;

    }


    public AppUser login2(long id) {

        AppUser data = jdbcClient.sql("""
                    select * from sc_app_user a
                             left join COUNTRIES c on c.COUNTRY_ID = a.country_id where id = :Id
                """).param("Id",id).query(AppUser.class).single();

        return data;
    }

    public LoginResponse login3(long id) {

        LoginResponse data = jdbcClient.sql("""
                    select a.ID , a.HEAD_FAMILY_NAME, c.NAME countryName from sc_app_user a
                            left join COUNTRIES c on c.COUNTRY_ID = a.country_id where id = :Id
                """).param("Id",id).query(LoginResponse.class).single();

        return data;
    }

    public LoginResponse login4(AppUserRequest2 data) {



        LoginResponse result = jdbcClient.sql("""
                    select a.ID , a.HEAD_FAMILY_NAME, c.NAME countryName from sc_app_user a
                            left join COUNTRIES c on c.COUNTRY_ID = a.country_id where MOBILE1 like :mobile limit 1 -- fetch first record
                """).param("mobile",data.mobile()).query(LoginResponse.class).single();

        return result;

    }
}
