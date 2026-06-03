package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.WhoAmI;

import java.time.LocalDateTime;

public record MessagesResponse(
        String MESSAGES,
        WhoAmI whoAmI,
        Long SENDER,
        LocalDateTime createDate
) {
}
