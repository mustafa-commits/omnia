package com.sc.demo.model.dto.chat;

import com.sc.demo.model.chat.AppChatDetails;

public record AppChatRequest(
        Long createBy,
        String chatTitle,
        AppChatDetails appChatDetails
) {
}
