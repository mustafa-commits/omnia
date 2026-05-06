package com.sc.demo.model.dto.Login;

public record VerificationLoginResponse(
        Long UserId,
        Integer sendingType,
        String Mobile
) {
}
