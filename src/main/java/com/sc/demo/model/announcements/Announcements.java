package com.sc.demo.model.announcements;

import com.sc.demo.model.notification.NotificationType;
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
    Long announcements_id;

    Integer sendId;

    String title;

    String description;

    Integer isActive;

    LocalDateTime createDate;

    LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "announcements", cascade = CascadeType.ALL)
    private List<AnnouncementsDetails> announcementsDetails = new ArrayList<>();

    public Announcements(Integer sendId, String title, String description) {
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
