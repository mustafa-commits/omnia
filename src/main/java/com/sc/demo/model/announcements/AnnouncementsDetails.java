package com.sc.demo.model.announcements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public AnnouncementsDetails(Long userId, Announcements announcements) {
        this.userId = userId;
        this.announcements = announcements;
    }
}
