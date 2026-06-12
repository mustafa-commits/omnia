package com.sc.demo.model.employees;

import com.sc.demo.model.notification.NotificationMaster;
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

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

    public PrivilegesDashboard(AccessToDashboard dashboardUserId, String userPrivilege,
                               String privilegeName, String createBy) {
        this.dashboardUserId = dashboardUserId;
        this.userPrivilege = userPrivilege;
        this.privilegeName = privilegeName;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
