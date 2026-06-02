package com.sc.demo.repository.familyInfo;

import com.sc.demo.model.familyInfo.familyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyInfoRepo extends JpaRepository<familyInfo, Long> {
}
