package com.sc.demo.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// تضاف البيانات في هذا الجدول للاشخاص الي سجلو دخول للتطبيق
@Entity
@Table(name = "sc_app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long headFamilyId;

    private Long requestId;

    private String Phone;

    private String branches;

    @Enumerated(EnumType.ORDINAL)
    private PhoneType phoneType;

    private LocalDateTime lastUse;

    @Column(length = 500)
    private String guardianName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public AppUser(String phone, Long requestId, Long headFamilyId, String branches, String guardianName, PhoneType phoneType) {
        Phone = phone;
        this.requestId = requestId;
        this.headFamilyId = headFamilyId;
        this.branches = branches;
        this.guardianName = guardianName;
        this.phoneType = phoneType;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }

}
