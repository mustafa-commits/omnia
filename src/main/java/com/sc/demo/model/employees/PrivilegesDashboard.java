package com.sc.demo.model.employees;

import com.sc.demo.model.notification.NotificationMaster;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private LocalDate createDate;

    private Long createBy;

    private LocalDate lastUpdate;

    private String lastUpdateBy;

    public PrivilegesDashboard(AccessToDashboard dashboardUserId, String userPrivilege,
                               String privilegeName, Long createBy) {
        this.dashboardUserId = dashboardUserId;
        this.userPrivilege = userPrivilege;
        this.privilegeName = privilegeName;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDate.now(); }

}
