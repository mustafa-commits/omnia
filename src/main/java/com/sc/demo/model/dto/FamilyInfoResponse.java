package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record FamilyInfoResponse(
       Long FamilyPersonId,
       String PersonFullName,
       Long RelationId,
       LocalDateTime BirthDate,
       Long Gender,
       String Phone1,
       String Phone2,
       String Phone3,
       Long NoOfMambers,
       Long FamilyNumberChildren,
       LocalDateTime Startdate,
       String Neighborhood,
       String NearsetPlace,
       String PlaceOfRequest,
       Long Age,
       String AcademicAchievement,
       String HouseLegal,
       String HealthStatus,
       String IllnessType,
       String IllnessDescription
) {
}
