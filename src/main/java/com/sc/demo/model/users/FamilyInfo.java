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
    private Long FamilyInfoid;

    private String FamilyNameMambers;

    private String mobile1;

    private String mobile2;

    private String mobile3;

    public FamilyInfo(String familyNameMambers, String mobile1, String mobile2, String mobile3) {
        FamilyNameMambers = familyNameMambers;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.mobile3 = mobile3;
    }
}
