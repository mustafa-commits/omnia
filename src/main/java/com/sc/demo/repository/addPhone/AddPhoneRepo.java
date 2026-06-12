package com.sc.demo.repository.addPhone;

import com.sc.demo.model.familyInfo.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddPhoneRepo extends JpaRepository<FamilyInfo, Long> {
}
