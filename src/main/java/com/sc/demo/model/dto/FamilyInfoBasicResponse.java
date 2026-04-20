package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record FamilyInfoBasicResponse(
       String FamilyNo,
       Long FamilyPersonsId,
       String PersonsFullName,
       String RelationshipType,
       Long PersonsAge,
       String HealthStatus,
       String HealthConditionDetails
) {
}
