package com.sc.demo.model.dto.familyInfo;

public record FamilyInfoHousingResponse(
        String GovernorateName,
        String JudgeName,
        String CityName,
        String Neighborhood,
        String NearestAddress,
        String BranchName,
        String OfficeName,
        String Phone1,
        String Phone2,
        String Phone3,
        String HousingType
) {
}
