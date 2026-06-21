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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "WHO_IS_SENDER")
    private WhoIsSender whoIsSender;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PLATFORM")
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

    private ConfirmProcedure confirmProcedure;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private AppChatMaster chatApp;

    public AppChatDetails(Long createBy, WhoIsSender whoIsSender, Platform platform,
                          String messages, AppChatMaster appChatMaster) {
        this.createBy = createBy;
        this.whoIsSender = whoIsSender;
        this.platform = platform;
        this.messages = messages;
        this.chatApp = appChatMaster;
    }

    public AppChatDetails(AppChatMaster chatApp, Long createBy, WhoIsSender whoIsSender,
                          Platform platform, String messages, MsgType msgType) {
        this.chatApp = chatApp;
        this.createBy = createBy;
        this.whoIsSender = whoIsSender;
        this.platform = platform;
        this.messages = messages;
        this.msgType = msgType;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
