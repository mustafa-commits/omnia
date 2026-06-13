package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_notification_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationDetailsId;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private NotificationMaster notification;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public NotificationDetails(Long userId, Long createBy, NotificationMaster notificationMaster) {
        this.userId = userId;
        this.createBy = createBy;
        this.notification = notificationMaster;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
