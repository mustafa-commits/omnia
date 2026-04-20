package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record FamilyInfoBasicResponse(
       String FamilyNo,
       Long FamilyPersonsId,
       String PersonsFullName,
       String RelationshipType,
       Long PersonsAge,
       String Phone1,
       String Phone2,
       String Phone3,
       String HousingType,
       String HealthStatus,
       String HealthConditionDetails
) {
}
