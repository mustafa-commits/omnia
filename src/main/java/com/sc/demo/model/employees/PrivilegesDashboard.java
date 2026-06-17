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
public class PrivilegesDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privilegeId;

    @ManyToOne
    @JoinColumn(name = "dashboard_fk")
    private AccessToDashboard dashboardUserId;

    private String userPrivilege;

    private String privilegeName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public PrivilegesDashboard(AccessToDashboard dashboardUserId, String userPrivilege,
                               String privilegeName, Long createBy) {
        this.dashboardUserId = dashboardUserId;
        this.userPrivilege = userPrivilege;
        this.privilegeName = privilegeName;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
