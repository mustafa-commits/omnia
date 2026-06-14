package com.sc.demo.repository.login;

import com.sc.demo.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    boolean existsByHeadFamilyIdAndRequestId (
            Long headFamilyId,
            Long requestId
    );
    AppUser findByHeadFamilyIdAndRequestId(Long headFamilyId, Long requestId);
}
