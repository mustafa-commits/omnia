package com.sc.demo.model.dto.search;

import java.time.LocalDate;

public record SearchResponseV3(
        String guardianName,
        String headFamilyName,
        Long familyPersonsId,
        LocalDate birthDate,
        Long requestId,
        String branches,
        String oldFamilyNo
) {
}
