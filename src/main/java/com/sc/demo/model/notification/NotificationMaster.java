package com.sc.demo.model.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
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

    @Enumerated(EnumType.ORDINAL)
    private SendingType sendingType;

    private Integer isActive = 1;

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDetails> notificationDetails = new ArrayList<>();

    public NotificationMaster(Integer sendId, String title,
                              String description, SendingType sendingType,
                              Long createBy) {
        this.sendId = sendId;
        this.title = title;
        this.description = description;
        this.sendingType = sendingType;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDate.now();
    }
}


