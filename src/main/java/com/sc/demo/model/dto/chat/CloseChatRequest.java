package com.sc.demo.model.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.demo.model.chat.ConfirmProcedure;

import java.time.LocalDateTime;

public record CloseChatRequest(
        Long chatId,
//        @JsonFormat(pattern = "M/d/yyyy h:mm:ss.SSSSSSSSS a")
        LocalDateTime createDate,
        ConfirmProcedure confirmProcedure
) {
}
