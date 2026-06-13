package com.sc.demo.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "sc_app_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    // تضاف البيانات في هذا الجدول للاشخاص الي سجلو دخول للتطبيق
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    private Long headFamilyId;

    private Long requestId;

    private String Phone;

    private String branches;

    @Enumerated(EnumType.ORDINAL)
    private PhoneType phoneType;

    private Integer isActive = 1;

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    public AppUser(String phone, Long requestId, Long headFamilyId, Long createBy,
                   PhoneType phoneType, String branches) {
        Phone = phone;
        this.requestId = requestId;
        this.headFamilyId = headFamilyId;
        this.createBy = createBy;
        this.phoneType = phoneType;
        this.branches = branches;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDate.now();
    }

}
