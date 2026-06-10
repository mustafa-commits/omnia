package com.sc.demo.model.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum MsgType {
    MESSAGE,
    IMAGE,
    VOICE
}
