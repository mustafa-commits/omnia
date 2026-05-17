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

    private Long msgType;

    private LocalDateTime seenAt;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public appChatDetails(Long sender, Long receiver, Long msgType, LocalDateTime seenAt, String createBy) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgType = msgType;
        this.seenAt = seenAt;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
