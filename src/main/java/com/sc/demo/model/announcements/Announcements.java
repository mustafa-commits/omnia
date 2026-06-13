package com.sc.demo.model.announcements;

import com.sc.demo.model.notification.SendingType;
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
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcements_id;

    private Long sendId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SENDING_TYPE")
    private SendingType sendingType;

    private String title;

    private String description;

    private Integer isActive;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "BRANCHES")
    private Branches branches;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    private Long createBy;

    private Long lastCreateBy;

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<AnnouncementsDetails> announcementsDetails = new ArrayList<>();

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<AnnouncementsAttachment> announcementsAttachment = new ArrayList<>();

    public Announcements(Long sendId, String title, String description, Branches branches, SendingType sendingType) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.isActive = 1;
        this.branches = branches;
        this.sendingType = sendingType;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
