package com.sc.demo.repository.Login;

import com.sc.demo.model.verification.VerificationApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationLoginRepo extends JpaRepository<VerificationApp, Long> {
}
