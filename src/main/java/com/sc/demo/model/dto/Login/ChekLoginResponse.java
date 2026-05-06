package com.sc.demo.model.dto.Login;

public record ChekLoginResponse(
        Long phone_Number,
        Long secretCode
) {
}
