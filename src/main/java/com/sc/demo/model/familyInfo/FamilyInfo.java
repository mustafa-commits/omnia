package com.sc.demo.model.familyInfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

//    @Column(unique=true)
    private Long headFamilyId;

    private Long requestId;

    private String headFamilyName;

    private LocalDate birthDate;

    private String Phone;

    private String oldFamilyNo;

    @Column(length = 500)
    private String guardianName;

    private String branches;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public FamilyInfo(String guardianName,Long headFamilyId, Long requestId, String headFamilyName,
                      Long createBy, LocalDate birthDate, String phone,
                      String oldFamilyNo, String branches) {
        this.guardianName = guardianName;
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
