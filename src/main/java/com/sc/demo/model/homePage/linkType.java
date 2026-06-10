package com.sc.demo.model.homePage;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum linkType {
    WEBLINK,
    PAGELINK
}
