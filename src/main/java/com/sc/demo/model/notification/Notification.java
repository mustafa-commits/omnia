package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    Integer sendId;

    String title;

    String description;

    Integer isActive;

    LocalDateTime createDate;

    LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDetails> notificationDetails;

    public Notification(Integer sendId, String title,
                        String description, List<NotificationDetails> notificationDetails) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.notificationDetails = notificationDetails;
        this.isActive=1;
    }

    @PrePersist
    public void prePersist(){
        this.createDate=LocalDateTime.now();
    }
}


