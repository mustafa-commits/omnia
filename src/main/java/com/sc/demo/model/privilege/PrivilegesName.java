package com.sc.demo.model.privilege;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum PrivilegesName {
    ADMINISTRATION,
    SUPPORTANSWERS,
    PUBLISHING
}
