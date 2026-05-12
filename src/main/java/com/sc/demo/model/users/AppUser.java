package com.sc.demo.model.users;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    private Long headFamilyId;

    private Long requestId;

    private LocalDateTime createDate;

    private Integer isActive = 1;

    private String Phone;

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
