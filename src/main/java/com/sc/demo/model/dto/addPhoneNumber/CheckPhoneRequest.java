package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDate;

public record CheckPhoneRequest(
        String headFamilyName,
        String guardianName,
        LocalDate birthDate,
        Long requestId,
        Long headFamilyId,
        String branches,
        String oldFamilyNo
) {
}
