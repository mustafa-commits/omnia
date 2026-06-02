package com.sc.demo.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sc_chat_master")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class appChatMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long userId;

    private String chatTitle;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "chatApp", cascade = CascadeType.ALL)
    private List<appChatDetails> appChatDetails = new ArrayList<>();

    public appChatMaster(Long userId, String chatTitle) {
        this.userId = userId;
        this.chatTitle = chatTitle;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
