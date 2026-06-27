package com.sc.demo.model.dto.token;

public record TokenRequest(
        String token,
        Long userId
) {
}
