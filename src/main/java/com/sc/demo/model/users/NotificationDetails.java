package com.sc.demo.model.users;

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
public class NotificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_details_id;

    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
