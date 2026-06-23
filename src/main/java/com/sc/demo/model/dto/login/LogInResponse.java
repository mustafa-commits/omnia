package com.sc.demo.model.dto.login;

public record LogInResponse(
        Long headFamilyId,
        Long requestId,
        String Branches,
        String guardianName) {
}
