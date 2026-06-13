package com.sc.demo.model.verification;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum MethodType {
    SMS,
    WHATSAPP
}
