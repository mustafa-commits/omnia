package com.sc.demo.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_chat_master")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppChatMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long userId;

    private String chatTitle;

    private String chatDescription;

    private LocalDateTime createDate;

    public AppChatMaster(Long userId, String chatTitle, String chatDescription) {
        this.userId = userId;
        this.chatTitle = chatTitle;
        this.chatDescription = chatDescription;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
