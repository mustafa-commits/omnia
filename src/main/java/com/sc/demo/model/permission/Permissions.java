package com.sc.demo.model.permission;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sc_dashboard_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private PermissionGroup groupId;

    private String permissionName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public Permissions(String permissionName, PermissionGroup groupId, Long createBy) {
        this.groupId = groupId;
        this.permissionName = permissionName;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
