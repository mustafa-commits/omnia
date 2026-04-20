package com.sc.demo.repository;

import com.sc.demo.model.users.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyInfoRepo extends JpaRepository<FamilyInfo, Long> {
}
