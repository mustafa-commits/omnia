package com.sc.demo.model.announcements;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SENDING_TYPE")
    private SendingType sendingType;

    private String title;

    @Column(length = 4000)
    private String description;

    private String branches;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<AnnouncementsDetails> announcementsDetails = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<AnnouncementsAttachment> announcementsAttachment = new ArrayList<>();

    public Announcements(String title, String description,
                         String branches, SendingType sendingType,
                         Long createBy) {
        this.title = title;
        this.description = description;
        this.branches = branches;
        this.sendingType = sendingType;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
