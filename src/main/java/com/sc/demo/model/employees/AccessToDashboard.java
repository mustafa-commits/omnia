package com.sc.demo.model.employees;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sc_dashboard_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dashboardUserId;

    @OneToMany(mappedBy = "dashboardUserId", cascade = CascadeType.ALL)
    private List<PrivilegesDashboard> privilegesDashboards = new ArrayList<>();

    @Column(length = 50)
    private String userName;

    @Column(length = 100)
    private String fullName;

    private String Phone;

    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Department departmentId;

    private PrivilegesName privilegesName;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public AccessToDashboard(String phone, Department departmentId, String password,
                             String userName, String fullName, PrivilegesName privilegesName,
                             Long createBy) {
        Phone = phone;
        this.departmentId = departmentId;
        this.password = password;
        this.userName = userName;
        this.fullName = fullName;
        this.privilegesName = privilegesName;
        this.createBy = createBy;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
