package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.chat.WhoAmI;

public record MessagesRequest(
        Long chatId,
        Long sender,
        Platform platform,
        String messages,
        MsgType msgType

) {
}
