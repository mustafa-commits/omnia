package com.sc.demo.model.dto.DashboardUser;

public record UserDashboardResponse(
        String phone,
        Long departmentId,
        String password,
        String userName,
        String fullName
) {
}
