package com.sc.demo.model.dto.homePage;

import com.sc.demo.model.homePage.linkType;

public record homePageResponse(
        Long photoId,
        linkType linkType,
        String fileName,
        String link
) {
}
