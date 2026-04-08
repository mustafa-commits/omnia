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
    private Long announcements_details_id;

    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "announcements_id")
    private Announcements announcements;

    public AnnouncementsDetails(Long user_id) {
        this.user_id = user_id;
    }

    public AnnouncementsDetails(Long user_id, Announcements announcements) {
        this.user_id = user_id;
        this.announcements = announcements;
    }
}
