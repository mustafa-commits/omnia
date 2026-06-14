package com.sc.demo.model.dto.login;

public record LogInResponse(
        Long HeadFamilyId,
        Long RequestId,
        String Branches
) {
}
