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

    public HomePagePhoto(String fileName, LinkType linkType) {
        this.fileName = fileName;
        this.linkType = linkType;
    }
}
