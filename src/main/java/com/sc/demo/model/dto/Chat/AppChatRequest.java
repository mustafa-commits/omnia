package com.sc.demo.model.dto.Chat;

import com.sc.demo.model.chat.AppChatDetails;

import java.util.List;

public record AppChatRequest(
        Long userId,
        String chatTitle,
        AppChatDetails appChatDetails
) {
}
