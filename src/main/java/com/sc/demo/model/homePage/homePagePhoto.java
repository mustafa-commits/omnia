package com.sc.demo.model.homePage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_homepage_photo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class homePagePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String fileName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "")
    private linkType linkType;

    private String link;

    private LocalDateTime createDate;

    public homePagePhoto(String fileName, linkType linkType, String link) {
        this.fileName = fileName;
        this.linkType = linkType;
        this.link = link;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
