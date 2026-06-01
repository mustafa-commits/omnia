package com.sc.demo.model.dto.homePage;

import com.sc.demo.model.homePage.LinkType;

public record HomePageRequest(
        String fileName,
        LinkType linkType,
        String link
) {
}
