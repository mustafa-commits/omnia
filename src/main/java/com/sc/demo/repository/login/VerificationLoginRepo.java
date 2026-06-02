package com.sc.demo.repository.login;

import com.sc.demo.model.verification.verificationApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationLoginRepo extends JpaRepository<verificationApp, Long> {
}
