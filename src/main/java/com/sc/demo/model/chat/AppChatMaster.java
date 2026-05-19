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
public class AppChatMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long userId;

    private String chatTitle;

    private String chatDescription;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "chatApp", cascade = CascadeType.ALL)
    private List<AppChatDetails> appChatDetails = new ArrayList<>();

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
