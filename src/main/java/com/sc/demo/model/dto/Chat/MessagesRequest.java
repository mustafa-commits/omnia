package com.sc.demo.model.dto.Chat;

public record MessagesRequest(
        Long sender,
        Long receiver,
        String messages

) {
}
