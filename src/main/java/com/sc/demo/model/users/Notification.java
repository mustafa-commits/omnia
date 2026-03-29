package com.sc.demo.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notification_id;

    Integer receiveId;

    Integer sendId;

    String title;

    String description;

    Integer isActive;

    LocalDateTime createDate;

    LocalDateTime lastUpdate;

    public Notification(Integer receiveId, Integer sendId, String title, String description) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.title = title;
        this.description = description;
    }
}


