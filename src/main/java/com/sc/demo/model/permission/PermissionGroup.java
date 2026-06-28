package com.sc.demo.model.permission;

import com.sc.demo.model.userDashboard.UserDashboard;
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
    @JoinColumn(name = "userId")
    private UserDashboard userId;

    @ManyToOne
    @JoinColumn(name = "permissionId")
    private Permissions permissionId;

    private String groupName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public PermissionGroup(String groupName, Long createBy) {
        this.groupName = groupName;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
