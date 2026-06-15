package com.sc.demo.model.dto.login;

public record GetDashboardUserIdWithToken(
        Long dashboardUserId,
        String token
) {
}
