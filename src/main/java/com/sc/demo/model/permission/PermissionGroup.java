package com.sc.demo.model.permission;

import com.sc.demo.model.employees.DashboardUsers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_dashboard_group_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "dashboardUsers")
    private DashboardUsers dashboardUsers;

    @ManyToOne
    @JoinColumn(name = "permissionsGroupId")
    private Permissions permissions;

    private String permissionName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
