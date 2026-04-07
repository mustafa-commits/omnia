package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Enumerated(EnumType.ORDINAL)
    NotificationType notificationType;

    LocalDateTime createDate;

    LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDetails> notificationDetails = new ArrayList<>();

    public Notification(Integer sendId, String title,
                        String description, List<NotificationDetails> notificationDetails,
                        NotificationType notificationType) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.notificationDetails.addAll(notificationDetails);
//        notificationDetails.get(0).setNotification(this);
        this.isActive=1;
        this.notificationType = notificationType;
    }
//        this.notificationDetails = notificationDetails;

    @PrePersist
    public void prePersist(){
        this.createDate=LocalDateTime.now();
    }
}


