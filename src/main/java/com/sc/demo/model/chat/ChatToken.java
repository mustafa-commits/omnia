package com.sc.demo.model.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_chat_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatToken {

    @Id
    private Long chatId;

    private String token;

    private Long tokenType;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public ChatToken(Long chatId, String token, Long tokenType, Long createBy) {
        this.chatId = chatId;
        this.token = token;
        this.tokenType = tokenType;
        this.createBy = createBy;
    }

    @PrePersist
    public void PrePersist(){
        this.createDate = LocalDateTime.now();
    }
}
