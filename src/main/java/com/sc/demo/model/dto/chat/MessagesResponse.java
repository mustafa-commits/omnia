package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.WhoIsSender;

import java.time.LocalDate;

public record MessagesResponse(
        String messages,
        WhoIsSender whoIsSender,
        Long useridSender,
        LocalDate createDate
) {
}
