package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.MsgType;
import com.sc.demo.model.chat.ReceiverFrom;

public record MessagesRequest(
        Long chatId,
        Long sender,
        Long receiver,
        ReceiverFrom receiverFrom,
        String messages,
        MsgType msgType

) {
}
