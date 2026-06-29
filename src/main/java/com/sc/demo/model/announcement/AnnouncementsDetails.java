package com.sc.demo.model.announcement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_announcements_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementsDetailsId;

    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcements_id")
    private Announcements announcements;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public AnnouncementsDetails(Long userId, Long createBy, Announcements announcements) {
        this.userId = userId;
        this.createBy = createBy;
        this.announcements = announcements;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
