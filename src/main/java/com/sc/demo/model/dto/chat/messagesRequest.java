package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.msgType;
import com.sc.demo.model.chat.platform;

public record messagesRequest(
        Long chatId,
        Long sender,
        Long receiver,
        platform platform,
        String messages,
        msgType msgType

) {
}
