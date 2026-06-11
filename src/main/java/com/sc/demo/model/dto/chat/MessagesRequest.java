package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;

public record MessagesRequest(
        Long chatId,
        Long userIdSender,
        Platform platform,
        String messages,
        MsgType msgType
) {
}
