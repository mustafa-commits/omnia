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
    Long notification_details_id;

    Long user_id;

    Long notification_id;
}
