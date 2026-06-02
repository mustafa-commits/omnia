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
public class notificationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    private Integer sendId;

    private String title;

    private String description;

    private Integer isActive;

    @Enumerated(EnumType.ORDINAL)
    private notificationType notificationType;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastCreateBy;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<notificationDetails> notificationDetails = new ArrayList<>();

    public notificationMaster(Integer sendId, String title,
                              String description, notificationType notificationType) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.isActive = 1;
        this.notificationType = notificationType;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}


