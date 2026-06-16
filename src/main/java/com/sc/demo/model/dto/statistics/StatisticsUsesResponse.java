package com.sc.demo.model.dto.statistics;

public record StatisticsUsesResponse(
        Long totalUsers,
        Long usersToday,
        Long downloadsToday,
        Long iosPct,
        Long androidPct
) {
}
