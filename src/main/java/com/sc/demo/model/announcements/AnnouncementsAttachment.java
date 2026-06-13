package com.sc.demo.model.announcements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Table(name = "sc_announcements_attachment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementsAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcements_details_id;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "announcements_id")
    private Announcements announcements;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public AnnouncementsAttachment(String fileName, Announcements announcements, Long createBy) {
        this.fileName = fileName;
        this.announcements = announcements;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
