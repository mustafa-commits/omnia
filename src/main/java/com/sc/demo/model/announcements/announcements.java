package com.sc.demo.model.announcements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sc_announcements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcements_id;

    private Integer sendId;

    private String title;

    private String description;

    private Integer isActive;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    private Long createBy;

    private Long lastCreateBy;

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<announcementsDetails> announcementsDetails = new ArrayList<>();

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<announcementsAttachment> announcementsAttachment = new ArrayList<>();

    public announcements(Integer sendId, String title, String description) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.isActive = 1;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
