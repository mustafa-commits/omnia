package com.sc.demo.model.users;

import com.sc.demo.model.announcements.Branches;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

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

    private LocalDateTime createDate;

    private Integer isActive = 1;

    private String Phone;

//    @Enumerated(EnumType.ORDINAL)
//    @Column(name = "BRANCHES")
    private Branches branches;

    public AppUser(String phone, Long requestId, Long headFamilyId) {
        Phone = phone;
        this.requestId = requestId;
        this.headFamilyId = headFamilyId;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }

}
