package com.sc.demo.model.dto.homePage;

import com.sc.demo.model.homePage.linkType;

public record homePageRequest(
        String link,
        linkType linkType

) {
}
