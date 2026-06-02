package com.sc.demo.model.chat;

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
public class appChatDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsChatId;

    private Long sender;

    private Long receiver;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RECEIVER_FROM")
    private platform platform = com.sc.demo.model.chat.platform.APP;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "MSG_ACTIVITY")
    private msgActivity msgActivity = com.sc.demo.model.chat.msgActivity.ACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "MSG_TYPE")
    private msgType msgType = com.sc.demo.model.chat.msgType.MESSAGE;

    @Column(length = 500)
    private String messages;

    private LocalDateTime seenAt;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private appChatMaster chatApp;

    public appChatDetails(Long sender, Long receiver, platform platform, String messages, appChatMaster appChatMaster) {
        this.sender = sender;
        this.receiver = receiver;
        this.platform = platform;
        this.messages = messages;
        this.chatApp = appChatMaster;
    }

    public appChatDetails(appChatMaster chatApp, Long sender, Long receiver, platform platform, String messages, msgType msgType) {
        this.chatApp = chatApp;
        this.sender = sender;
        this.receiver = receiver;
        this.platform = platform;
        this.messages = messages;
        this.msgType = msgType;
    }

    public appChatDetails(Long sender, String messages) {
        this.sender = sender;
        this.platform = com.sc.demo.model.chat.platform.DASHBOARD;
        this.messages = messages;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
