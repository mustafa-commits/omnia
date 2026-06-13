package com.sc.demo.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
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

    private Integer isActive = 1;

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    @OneToMany(mappedBy = "chatApp", cascade = CascadeType.ALL)
    private List<AppChatDetails> appChatDetails = new ArrayList<>();

    public AppChatMaster(Long userId, String chatTitle, Long createBy) {
        this.userId = userId;
        this.chatTitle = chatTitle;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDate.now();
    }
}
