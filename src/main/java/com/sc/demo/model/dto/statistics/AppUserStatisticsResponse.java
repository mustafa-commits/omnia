package com.sc.demo.model.dto.statistics;

import com.sc.demo.model.users.PhoneType;

import java.time.LocalDateTime;

public record AppUserStatisticsResponse(
        String phone,
        String branches,
        String guardianName,
        PhoneType phoneType,
        LocalDateTime lastUse,
        Long familyPersonsId
) {
}
