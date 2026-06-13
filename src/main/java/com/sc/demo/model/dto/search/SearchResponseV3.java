package com.sc.demo.model.dto.search;

import java.time.LocalDateTime;

public record SearchResponseV3(
        String guardianName,
        String headFamilyName,
        Long familyPersonsId,
        LocalDateTime birthDate,
        Long requestId,
        String branches,
        String oldFamilyNo
) {
}
