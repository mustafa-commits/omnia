package com.sc.demo.model.FamilyInfo;

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

    private Long userId;

    private String headFamilyName;

    private String oldFamilyNo;

    private String phone;

    private Integer activeApp;

    private LocalDateTime createDate;

    private Integer addingBy;

    private LocalDateTime birthDate;

    public FamilyInfo(String headFamilyName, String oldFamilyNo, String phone, Long activeApp, Integer addingBy) {
        this.headFamilyName = headFamilyName;
        this.oldFamilyNo = oldFamilyNo;
        this.phone = phone;
        this.activeApp = 1;
        this.addingBy = addingBy;
    }

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }
}
