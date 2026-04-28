package com.sc.demo.repository;

import com.sc.demo.model.Verification.VerificationApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationLoginRepo extends JpaRepository<VerificationApp, Long> {
}
