package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sc_notification_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class notificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationDetailsId;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private notificationMaster notification;

    public notificationDetails(Long userId, notificationMaster notificationMaster) {
        this.userId = userId;
        this.notification = notificationMaster;
    }
}
