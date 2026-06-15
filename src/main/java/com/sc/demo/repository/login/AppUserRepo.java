package com.sc.demo.repository.login;

import com.sc.demo.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    boolean existsByHeadFamilyIdAndRequestIdAndBranchesAndGuardianName (
            Long headFamilyId,
            Long requestId,
            String branches,
            String guardianName
    );
    AppUser findByHeadFamilyIdAndRequestId(Long headFamilyId, Long requestId);
}
