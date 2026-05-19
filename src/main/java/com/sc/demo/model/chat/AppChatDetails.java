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

    private String messages;

    private LocalDateTime seenAt;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private AppChatMaster chatApp;

    public AppChatDetails(Long sender, Long receiver, Long msgTyp, String messages, AppChatMaster appChatMaster) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgType = msgType;
        this.messages = messages;
        this.chatApp = appChatMaster;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
