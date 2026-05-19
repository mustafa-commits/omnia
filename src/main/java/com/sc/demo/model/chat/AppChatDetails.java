package com.sc.demo.model.chat;

import com.sc.demo.model.notification.NotificationMaster;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_chat_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppChatDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsChatId;

    private Long sender;

    private Long receiver;

    private Long msgType;

    private LocalDateTime seenAt;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppChatMaster chatApp;

    public AppChatDetails(Long sender, Long receiver, Long msgType, LocalDateTime seenAt,
                          String createBy, AppChatMaster appChatMaster) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgType = msgType;
        this.seenAt = seenAt;
        this.createBy = createBy;
        this.chatApp = appChatMaster;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
