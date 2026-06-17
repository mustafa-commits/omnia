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

    private String title;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private SendingType sendingType;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDetails> notificationDetails = new ArrayList<>();

    public NotificationMaster(Long createBy, String title,
                              String description, SendingType sendingType) {
        this.createBy = createBy;
        this.title = title;
        this.description = description;
        this.sendingType = sendingType;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}


