package com.sc.demo.model.dto;

public record FamilyInfoBasicResponse(
       String FamilyNo,
       Long FamilyPersonsId,
       String PersonsFullName,
       String RelationshipType,
       Long PersonsAge,
       String HealthStatus,
       String HealthConditionDetails,
       Long IsGuardian
) {
}
