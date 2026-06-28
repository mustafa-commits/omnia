package com.sc.demo.model.dto.DashboardUser;

import com.sc.demo.model.permission.PermissionGroup;

import java.util.List;

public record UserDashboardResponse(
        String phone,
        Long departmentId,
        String password,
        String userName,
        String fullName,
        List<PermissionGroup> permissionTemplate
) {
}
