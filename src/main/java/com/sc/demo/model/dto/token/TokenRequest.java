package com.sc.demo.model.dto.token;

import com.sc.demo.model.chat.Platform;

public record TokenRequest(
        Long userId,
        Platform tokenType,
        String token
) {
}
