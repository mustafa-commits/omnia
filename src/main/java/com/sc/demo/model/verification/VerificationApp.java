package com.sc.demo.model.verification;

import com.sc.demo.model.users.PhoneType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_verification_apps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationApp {

    // تضاف البيانات في هذا الجدول بعد التأكد من رقم الهاتف الصحيح وال Otp تم انشاءه
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Enumerated(EnumType.ORDINAL)
    private PhoneType phoneType;

    private String userIdentifier;

    private Long secretCode;

    @Enumerated(EnumType.ORDINAL)
    private MethodType methodType;

    private Integer isUsed = 0;

    private LocalDateTime timeUsed;

    private Integer isActive = 1;

    @CreatedDate
    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public VerificationApp(String userIdentifier, Long secretCode, MethodType methodType) {
        this.userIdentifier = userIdentifier;
        this.secretCode = secretCode;
        this.methodType = methodType;
    }

    public VerificationApp(PhoneType phoneType) {
        this.phoneType = phoneType;
        this.timeUsed = LocalDateTime.now();
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
