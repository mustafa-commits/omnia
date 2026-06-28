package com.sc.demo.model.dto.token;

public record TokenLoginRequest(
        Long userId,
        String token
) {
}
