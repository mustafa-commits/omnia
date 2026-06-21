package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.WhoIsSender;

import java.time.LocalDateTime;

public record MessagesResponse(
        String messages,
        WhoIsSender whoIsSender,
        Long createBy,
        LocalDateTime createDate,
        Long platForm
) {
}
