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
    private Long user_id;

    private String token;

    private Long tokenType;

    private Boolean isActive;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastCreateBy;

    public NotificationToken(Long user_id,  String token, Long tokenType, Boolean isActive) {
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
