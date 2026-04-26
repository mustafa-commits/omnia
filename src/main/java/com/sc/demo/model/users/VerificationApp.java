package com.sc.demo.model.users;

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
    private Long VerificationAppId;

    private Long UserId;

    private Long SecretCode;

    private LocalDateTime CreateDate;

    private Integer SendingType;

    public VerificationApp(Long userId, Long secretCode, Integer sendingType) {
        UserId = userId;
        SecretCode = secretCode;
        SendingType = sendingType;
    }


    @PrePersist
    public void prePersist(){
        this.CreateDate = LocalDateTime.now();
    }
}
