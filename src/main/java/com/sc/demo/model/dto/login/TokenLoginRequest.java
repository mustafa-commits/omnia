package com.sc.demo.model.dto.login;

public record TokenLoginRequest(
        Long userId,
        String token
) {
}
