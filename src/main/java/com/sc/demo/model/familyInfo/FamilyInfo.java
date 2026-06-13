package com.sc.demo.model.familyInfo;

import com.sc.demo.model.announcements.Branches;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_family_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;

    @Column(unique=true)
    private Long headFamilyId;

    private Long requestId;

    private String headFamilyName;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime birthDate;

    private String Phone;

    private String oldFamilyNo;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Branches branches;

    public FamilyInfo(Long headFamilyId, Long requestId, String headFamilyName,
                      Long createBy, LocalDateTime birthDate, String phone,
                      String oldFamilyNo, Branches branches) {
        this.headFamilyId = headFamilyId;
        this.requestId = requestId;
        this.headFamilyName = headFamilyName;
        this.createBy = createBy;
        this.birthDate = birthDate;
        Phone = phone;
        this.oldFamilyNo = oldFamilyNo;
        this.branches = branches;
    }


    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
