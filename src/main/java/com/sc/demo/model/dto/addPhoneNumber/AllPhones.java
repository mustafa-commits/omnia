package com.sc.demo.model.dto.addPhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AllPhones(
        Long infoId,
        String phone,
        LocalDate birthDate,
        String createBy,
        LocalDateTime createDate,
        String headFamilyName,
        String oldFamilyNo,
        String guardianName
) {
}
