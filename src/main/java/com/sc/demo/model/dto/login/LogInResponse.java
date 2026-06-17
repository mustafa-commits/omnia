package com.sc.demo.model.dto.login;

import com.sc.demo.model.users.PhoneType;

public record LogInResponse(
        Long headFamilyId,
        Long requestId,
        String Branches,
        String guardianName
) {
}
