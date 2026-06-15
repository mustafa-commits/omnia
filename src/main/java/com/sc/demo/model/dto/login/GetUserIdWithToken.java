package com.sc.demo.model.dto.login;

public record GetUserIdWithToken(
        Long userId,
        String token
) {
}
