package com.sc.demo.model.dto.DashboardUser;

public record DashboardUserRequest(
        Long userDashboardId,
        String PHONE,
        String fullName,
        String userName
) {
}
