package com.sc.demo.model.dto;

public record VerificationLoginResponse(
        Long UserId,
        Integer sendingType,
        String Mobile
) {
}
