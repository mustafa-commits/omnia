package com.sc.demo.model.dto.addPhoneNumber;

import com.sc.demo.model.announcements.Branches;

import java.time.LocalDateTime;

public record AddPhonRequest(
        Long headFamilyId,
        Long requestId,
        String headFamilyName,
        Long createBy,
        LocalDateTime birthDate,
        String phone,
        String oldFamilyNo,
        Branches branches
) {
}
