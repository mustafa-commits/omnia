package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_notification_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notification_token_id;

    @OneToOne
    @JoinColumn(name = "fk_appUser_id")
    Long user_id;

    String token;

    Long tokenType;

    Boolean isActive;

    LocalDateTime createDate;

    Long createBy;

    LocalDateTime lastUpdate;

    Long lastCreateBy;

    public NotificationToken(Long notification_token_id, Long user_id, String token, Long tokenType, Boolean isActive) {
        this.notification_token_id = notification_token_id;
        this.user_id = user_id;
        this.token = token;
        this.tokenType = tokenType;
        this.isActive = true;
    }

    @PrePersist
    public void PrePersist(){
        this.createDate = LocalDateTime.now();
    }
}
