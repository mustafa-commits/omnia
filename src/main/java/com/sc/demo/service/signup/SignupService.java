package com.sc.demo.service.signup;

import com.sc.demo.model.users.AppUser;
import com.sc.demo.model.users.dto.CreateUser;
import com.sc.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    @Autowired
    private AppUserRepo appUserRepo;


    public long signup(CreateUser createUser) {
        return appUserRepo.save(
                new AppUser(createUser.name(), createUser.mobile())
        ).getId();
    }



}
