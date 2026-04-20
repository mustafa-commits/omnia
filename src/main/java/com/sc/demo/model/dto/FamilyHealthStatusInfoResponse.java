package com.sc.demo.model.dto;

public record FamilyHealthStatusInfoResponse(
        String HouseLegal,
        String HealthStatus,
        String IllnessType,
        String IllnessDescription
) {
}
