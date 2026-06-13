package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_notification_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationToken {

    @Id
    private Long userId;

    @Column(length = 4000)
    private String token;

    private Long tokenType;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @PrePersist
    public void PrePersist(){
        this.createDate = LocalDateTime.now();
    }
}
