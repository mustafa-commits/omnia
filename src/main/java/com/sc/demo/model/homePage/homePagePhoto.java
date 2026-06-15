package com.sc.demo.model.homePage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_homepage_photos")
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
    @Column(name = "LINK_TYPE")
    private linkType linkType;

    private String link;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public homePagePhoto(String fileName, linkType linkType, String link, Long createBy) {
        this.fileName = fileName;
        this.linkType = linkType;
        this.link = link;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
