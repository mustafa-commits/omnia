package com.sc.demo.repository.login;

import com.sc.demo.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepo extends JpaRepository<AppUser, Long> {
}
