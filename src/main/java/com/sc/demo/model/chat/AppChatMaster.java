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

    private String chatTitle;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @OneToMany(mappedBy = "chatApp", cascade = CascadeType.ALL)
    private List<AppChatDetails> appChatDetails = new ArrayList<>();

    public AppChatMaster(Long createBy, String chatTitle) {
        this.createBy = createBy;
        this.chatTitle = chatTitle;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
