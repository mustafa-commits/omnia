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
public class VerificationApp {

    // تضاف البيانات في هذا الجدول بعد التأكد من رقم الهاتف الصحيح وال Otp تم انشاءه
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userIdentifier;

    private Long secretCode;

    private MethodType methodType;

    private Integer isUsed = 0;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public VerificationApp(String userIdentifier, Long secretCode, MethodType methodType) {
        this.userIdentifier = userIdentifier;
        this.secretCode = secretCode;
        this.methodType = methodType;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
