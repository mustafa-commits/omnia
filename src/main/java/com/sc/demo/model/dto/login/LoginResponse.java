package com.sc.demo.model.dto.login;

public record LoginResponse(
        Long headFamilyId,
        Long requestId,
        String Branches,
        String guardianName
) {
}
