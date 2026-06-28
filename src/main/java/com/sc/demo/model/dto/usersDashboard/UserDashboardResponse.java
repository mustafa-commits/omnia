package com.sc.demo.model.dto.usersDashboard;

public record UserDashboardResponse(
        String phone,
        Long departmentId,
        String password,
        String userName,
        String fullName,
        Long groupId
) {
}
