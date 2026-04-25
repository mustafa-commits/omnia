package com.sc.demo.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sc_family_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long FamilyInfoId;

    private String FamilyNameMambers;

    private String mobile1;

    private Long activeMobile1;

    private String mobile2;

    private Long activeMobile2;

    private String mobile3;

    private Long activeMobile3;

    public FamilyInfo(String familyNameMambers, String mobile1, Long activeMobile1,
                      String mobile2, Long activeMobile2, String mobile3, Long activeMobile3) {
        FamilyNameMambers = familyNameMambers;
        this.mobile1 = mobile1;
        this.activeMobile1 = activeMobile1;
        this.mobile2 = mobile2;
        this.activeMobile2 = activeMobile2;
        this.mobile3 = mobile3;
        this.activeMobile3 = activeMobile3;
    }
}
