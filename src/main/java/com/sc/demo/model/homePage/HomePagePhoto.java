package com.sc.demo.model.homePage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sc_homepage_photo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomePagePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String fileName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "")
    private LinkType linkType;

    private String link;

    public HomePagePhoto(String fileName, LinkType linkType, String link) {
        this.fileName = fileName;
        this.linkType = linkType;
        this.link = link;
    }
}
