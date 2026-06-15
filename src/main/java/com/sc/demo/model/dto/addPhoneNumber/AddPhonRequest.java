package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDate;

public record AddPhonRequest(
        String guardianName,
        Long headFamilyId,
        Long requestId,
        String headFamilyName,
        Long createBy,
        LocalDate birthDate,
        String phone,
        String oldFamilyNo,
        String branches
) {
}
