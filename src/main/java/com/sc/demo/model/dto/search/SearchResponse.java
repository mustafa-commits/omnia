package com.sc.demo.model.dto.search;

public record SearchResponse(
         Long FamilyPersonsId,
         String GuardianName,
         Long userId
) {
}
