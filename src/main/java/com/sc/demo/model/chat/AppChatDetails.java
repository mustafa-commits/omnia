package com.sc.demo.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDate;


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

    private Long userIdSender;

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

    private LocalDate seenAt;

    private Integer isActive = 1;

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private AppChatMaster chatApp;

    public AppChatDetails(Long userIdSender, WhoIsSender whoIsSender, Platform platform,
                          String messages, Long createBy, AppChatMaster appChatMaster) {
        this.userIdSender = userIdSender;
        this.whoIsSender = whoIsSender;
        this.platform = platform;
        this.messages = messages;
        this.createBy = createBy;
        this.chatApp = appChatMaster;
    }

    public AppChatDetails(AppChatMaster chatApp, Long userIdSender, WhoIsSender whoIsSender,
                          Platform platform, String messages, MsgType msgType, Long createBy) {
        this.chatApp = chatApp;
        this.userIdSender = userIdSender;
        this.whoIsSender = whoIsSender;
        this.platform = platform;
        this.messages = messages;
        this.msgType = msgType;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDate.now(); }
}
