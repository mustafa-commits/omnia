package com.sc.demo.model.dto.login;

import com.sc.demo.model.dto.permission.PermissionRequest;
import java.util.List;

public record LoginRequest2(
        Long userId,
        String userName,
        String groupName,
        List<PermissionRequest> permissions,
        String token
) {
}
