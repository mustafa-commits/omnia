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
public class AppChatDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsChatId;

    private Long sender;

    private Long receiver;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RECEIVER_FROM")
    private Platform platform = Platform.APP;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "MSG_ACTIVITY")
    private MsgActivity msgActivity = MsgActivity.ACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "MSG_TYPE")
    private MsgType msgType = MsgType.MESSAGE;

    @Column(length = 500)
    private String messages;

    private LocalDateTime seenAt;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private AppChatMaster chatApp;

    public AppChatDetails(Long sender, Long receiver, Platform platform, String messages, AppChatMaster appChatMaster) {
        this.sender = sender;
        this.receiver = receiver;
        this.platform = platform;
        this.messages = messages;
        this.chatApp = appChatMaster;
    }

    public AppChatDetails(AppChatMaster chatApp, Long sender, Long receiver, Platform platform, String messages, MsgType msgType) {
        this.chatApp = chatApp;
        this.sender = sender;
        this.receiver = receiver;
        this.platform = platform;
        this.messages = messages;
        this.msgType = msgType;
    }

    public AppChatDetails(Long sender, String messages) {
        this.sender = sender;
        this.platform = Platform.DASHBOARD;
        this.messages = messages;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
