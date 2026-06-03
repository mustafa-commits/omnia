package com.sc.demo.model.employees;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sc_dashboard_privileges")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class privilegesDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privilegeId;

    private Long dashboardUserId;

    private String userPrivilege;

    private String privilegeName;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
