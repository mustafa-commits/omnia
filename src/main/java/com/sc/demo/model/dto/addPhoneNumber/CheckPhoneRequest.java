package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDateTime;

public record CheckPhoneRequest(
        String headFamilyName,
        LocalDateTime birthDate,
        Long requestId,
        Long headFamilyId,
        String branches,
        String oldFamilyNo
) {
}
