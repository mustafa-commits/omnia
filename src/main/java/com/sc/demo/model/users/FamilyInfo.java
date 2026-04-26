package com.sc.demo.model.users;

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
    private Long InfoId;

    private Long UserId;

    private String HeadFamilyName;

    private String OldFamilyNo;

    private String Phone;

    private Integer ActiveApp;

    private LocalDateTime CreateDate;

    private Integer AddingBy;

    public FamilyInfo(String HeadFamilyName, String OldFamilyNo, String Phone, Long activeApp, Integer AddingBy) {
        HeadFamilyName = HeadFamilyName;
        this.OldFamilyNo = OldFamilyNo;
        this.Phone = Phone;
        this.ActiveApp = 1;
        this.AddingBy = AddingBy;
    }

    @PrePersist
    public void prePersist(){
        this.CreateDate = LocalDateTime.now();
    }
}
