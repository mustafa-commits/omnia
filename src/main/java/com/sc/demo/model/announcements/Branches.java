package com.sc.demo.model.announcements;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum Branches {
    HEADQUARTER,
    RUSAFA,
    KARADA,
    SAYDIA,
    ZAFRANIYA,
    BABIL,
    KARBALA,
    NAJAF,
    DEWANIYA,
    MAYSAN,
    DHIQAR,
    WASIT,
    MUTHANA,
    MSAYAB,
    BALAD,
    BASRAH,
    ZUBAIR,
    QAZANIYA,
    MDAYNA,
    ABOALKHASEB,
    ALHIRA,
    ALIALGHARBY,
    KHANQEEN
}
