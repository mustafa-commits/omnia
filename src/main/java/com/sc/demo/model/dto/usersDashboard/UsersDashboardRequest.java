package com.sc.demo.model.dto.usersDashboard;

public record UsersDashboardRequest(
        Long userId,
        String PHONE,
        String fullName,
        String userName
) {
}
