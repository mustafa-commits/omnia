package com.sc.demo.model.Tokens;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_app_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppToken {

    @Id
    private Long userId;

    @Column(length = 4000)
    private String token;

    private Long tokenType;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @PrePersist
    public void PrePersist(){
        this.createDate = LocalDateTime.now();
    }
}
