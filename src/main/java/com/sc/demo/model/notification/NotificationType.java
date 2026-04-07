package com.sc.demo.model.notification;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum NotificationType {

    PRIVATE,
    PUBLIC

}
