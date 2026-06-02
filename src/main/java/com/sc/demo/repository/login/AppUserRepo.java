package com.sc.demo.repository.login;

import com.sc.demo.model.users.appUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<appUser, Long> {


}
