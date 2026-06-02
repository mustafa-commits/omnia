package com.sc.demo.model.verification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_verification_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class verificationApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userIdentifier;

    private Long secretCode;

    private LocalDateTime createDate;

    private sendingType sendingType;

    private Integer isUsed = 0;

    public verificationApp(String userIdentifier, Long secretCode, sendingType sendingType) {
        this.userIdentifier = userIdentifier;
        this.secretCode = secretCode;
        this.sendingType = sendingType;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
