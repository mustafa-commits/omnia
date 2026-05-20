package com.sc.demo.repository.FamilyInfo;

import com.sc.demo.model.familyInfo.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyInfoRepo extends JpaRepository<FamilyInfo, Long> {
}
