package com.sc.demo.model.announcements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "announcements_id")
    private Announcements announcements;

    private Integer isActive = 1;

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    public AnnouncementsDetails(Long userId, Announcements announcements, Long createBy) {
        this.userId = userId;
        this.announcements = announcements;
        this.createBy = createBy;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDate.now();
    }
}
