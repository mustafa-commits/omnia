package com.sc.demo.repository.familyInfo;

import com.sc.demo.model.verification.VerificationApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyInfoRepo extends JpaRepository<VerificationApp, Long> {
}
