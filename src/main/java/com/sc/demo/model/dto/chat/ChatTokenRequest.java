package com.sc.demo.model.dto.chat;

public record ChatTokenRequest(
        String token,
        Long userId
) {
}
