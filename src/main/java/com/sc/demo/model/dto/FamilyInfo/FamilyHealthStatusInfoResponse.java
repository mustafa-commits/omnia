package com.sc.demo.model.dto.FamilyInfo;

public record FamilyHealthStatusInfoResponse(
        String HouseLegal,
        String HealthStatus,
        String IllnessType,
        String IllnessDescription
) {
}
