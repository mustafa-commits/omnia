package com.sc.demo.model.announcements;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Branches {
    HEADQUARTER("01"),
    MUTHANNA("02"),
    KARRADA("03"),
    RUSAFA("04"),
    WASIT("05"),
    MAYSAN("06"),
    NAJAF("07"),
    KARBALA("08"),
    BABYLON("09"),
    BASRA_ZUBAIR("10"),
    BASRA_MADINA("11"),
    BASRA_CENTER("12"),
    BASRA_ABI_AL_KHASIB("13"),
    DIYALA_KHALIS("14"),
    DIWANIYAH("15"),
    KIRKUK("16"),
    TAL_AFAR("17"),
    DHI_QAR("18"),
    ABI_SAIDA("19"),
    DUJAIL("21"),
    QAZANIYA("22"),
    BALAD("23"),
    KHANAQIN("24"),
    HAMDANIYA("27"),
    MUSAYYIB("38"),
    KADHIMIYAH("39"),
    BAQUBA("41"),
    ERBIL("42"),
    SAYDIYA("43");


    @Autowired
    private String value;

    Branches(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
