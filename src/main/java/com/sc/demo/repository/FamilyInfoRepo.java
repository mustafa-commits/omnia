package com.sc.demo.repository;

import com.sc.demo.model.FamilyInfo.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyInfoRepo extends JpaRepository<FamilyInfo, Long> {
}
