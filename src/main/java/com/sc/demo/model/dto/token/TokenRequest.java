package com.sc.demo.model.dto.token;

public record TokenRequest(
        Long userId,
        String token
) {
}
