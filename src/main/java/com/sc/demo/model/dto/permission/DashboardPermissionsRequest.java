package com.sc.demo.model.dto.permission;

public record DashboardPermissionsRequest(
        Long groupId,
        String groupName,
        Long isActive,
        String permissionName
) {
}
