package com.sc.demo.model.dto.search;

public record SearchResponseV2(
        long familyPersonsId,
        String guardianName,
        String headFamilyName
) {
}
