package com.sc.demo.model.dto.login;

public record LoginRequest(
        Long userId,
        String userName,
        Long groupId,
        String groupName
) {
}
