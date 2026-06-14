package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDate;

public record AddPhonRequest(
        String headFamilyName,
        LocalDate birthDate,
        String phone,
        String oldFamilyNo
    ) {
}
