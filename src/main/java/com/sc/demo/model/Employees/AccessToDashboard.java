package com.sc.demo.model.Employees;

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
public class AccessToDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendId;

    private Long sendName;

    private LocalDateTime createDate;

    private String Phone;

    private String password;

    private String departmentId;

    public AccessToDashboard(String departmentId, String password, String phone, Long sendName) {
        this.departmentId = departmentId;
        this.password = password;
        Phone = phone;
        this.sendName = sendName;
    }
}
