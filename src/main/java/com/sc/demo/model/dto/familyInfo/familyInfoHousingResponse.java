package com.sc.demo.model.dto.familyInfo;

public record familyInfoHousingResponse(
        String GovernorateName,
        String JudgeName,
        String CityName,
        String Neighborhood,
        String NearsetAddress,
        String BranchName,
        String OfficeName,
        String Phone1,
        String Phone2,
        String Phone3,
        String HousingType
) {
}
