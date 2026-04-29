package com.sc.demo.model.Verification;

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
public class VerificationApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userIdentifier;

    private Long secretCode;

    private LocalDateTime createDate;

    private SendingType sendingType;

    private Integer isUsed = 0;

    public VerificationApp(String userIdentifier, Long secretCode, SendingType sendingType) {
        this.userIdentifier = userIdentifier;
        this.secretCode = secretCode;
        this.sendingType = sendingType;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
