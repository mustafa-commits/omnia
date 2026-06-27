package com.sc.demo.model.privilege;

import com.sc.demo.model.employees.DashboardUsers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sc_dashboard_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @OneToMany(mappedBy = "permissionGroup", cascade = CascadeType.ALL)
    private List<Permissions> privilegeGroupId = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Permissions privilegeGroupsId;

    private String privilegeName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
