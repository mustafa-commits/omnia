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
public class NotificationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    private Integer sendId;

    private String title;

    private String description;

    private Integer isActive;

    @Enumerated(EnumType.ORDINAL)
    private NotificationType notificationType;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDetails> notificationDetails = new ArrayList<>();

    @OneToOne(mappedBy = "notificationMaster", cascade = CascadeType.ALL)
    private NotificationToken notificationToken;

    public NotificationMaster(Integer sendId, String title,
                              String description, NotificationType notificationType) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.isActive = 1;
        this.notificationType = notificationType;
    }

    @PrePersist
    public void prePersist(){
        this.createDate=LocalDateTime.now();
    }
}


