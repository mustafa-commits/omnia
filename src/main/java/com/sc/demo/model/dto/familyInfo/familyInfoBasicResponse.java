package com.sc.demo.model.dto.familyInfo;

public record familyInfoBasicResponse(
       String FamilyNo,
       Long FamilyPersonsId,
       String PersonsFullName,
       Long RelationId,
       String RelationshipType,
       Long PersonsAge,
       String HealthStatus,
       String HealthConditionDetails,
       Long IsGuardian,
       String EductionGrades,
       String AcademicBranch,
       String StudyStatus,
       String SchoolName,
       String CaseStatus
) {
}
