package com.sc.demo.model.employees;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_dashboard_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class accessToDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long userName;

    private String Phone;

    private String password;

    private String departmentId;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public accessToDashboard(Long userName, String phone, String password, String departmentId) {
        this.userName = userName;
        Phone = phone;
        this.password = password;
        this.departmentId = departmentId;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
