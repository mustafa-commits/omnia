package com.sc.demo.model.dto.announcements;

public record TokenRequest(
        String token,
        Long userId
) {
}
