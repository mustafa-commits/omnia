package com.sc.demo.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sc_app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String headFamilyName;

    String mobile1;

    String mobile2;

    String address;

    Integer countryId;


    public AppUser(String headFamilyName, String mobile1) {
        this.headFamilyName = headFamilyName;
        this.mobile1 = mobile1;
    }
}
