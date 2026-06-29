package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDate;

public record AddPhonRequest(
        String guardianName,
        String headFamilyName,
        LocalDate birthDate,
        String phone,
        String branches,
        Long requestId,
        Long headFamilyId,
        String oldFamilyNo
) {
}
