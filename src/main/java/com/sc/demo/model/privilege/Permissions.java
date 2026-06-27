package com.sc.demo.model.privilege;

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
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privilegeId;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private PermissionGroup permissionGroup;

    private String userPrivilege;

    private PrivilegesName privilegeName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public Permissions(String userPrivilege, PrivilegesName privilegeName, Long createBy) {
        this.userPrivilege = userPrivilege;
        this.privilegeName = privilegeName;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
