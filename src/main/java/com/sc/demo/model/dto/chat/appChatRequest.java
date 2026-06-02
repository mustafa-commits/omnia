package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.appChatDetails;

public record appChatRequest(
        Long userId,
        String chatTitle,
        appChatDetails appChatDetails
) {
}
