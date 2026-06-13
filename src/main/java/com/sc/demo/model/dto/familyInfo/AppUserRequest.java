package com.sc.demo.model.dto.familyInfo;

import com.sc.demo.model.users.PhoneType;

public record AppUserRequest(
        String phone,
        Long secretCode/*,
        Long createBy,
        PhoneType phoneType,
        String branches*/
) {
}
