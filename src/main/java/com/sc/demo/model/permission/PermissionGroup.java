package com.sc.demo.model.permission;

import com.sc.demo.model.userDashboard.UserDashboard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "groupId", cascade = CascadeType.ALL)
    private List<UserDashboard> userId = new ArrayList<>();

    private String groupName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public PermissionGroup(String groupName, Long createBy) {
        this.groupName = groupName;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
