package com.sc.demo.model.dto.homePage;

import com.sc.demo.model.homePage.linkType;

public record homePageResponse(
        String fileName,
        linkType linkType,
        String link
) {
}
