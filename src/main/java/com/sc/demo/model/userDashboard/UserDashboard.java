package com.sc.demo.model.userDashboard;


import com.sc.demo.model.permission.PermissionGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sc_dashboard_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDashboardId;

    @OneToMany(mappedBy = "userDashboard", cascade = CascadeType.ALL)
    private List<PermissionGroup> permissionGroupId = new ArrayList<>();

    @Column(length = 50)
    private String userName;

    @Column(length = 100)
    private String fullName;

    private String Phone;

    private String password;

    private Long departmentId;

    private Integer isActive = 1;

    private LocalDateTime createDate;

    private Long createBy;

    private LocalDateTime lastUpdate;

    private Long lastUpdateBy;

    public UserDashboard(String phone, Long departmentId, String password,
                         String userName, String fullName, List<PermissionGroup> permissionTemplate, Long createBy) {
        Phone = phone;
        this.departmentId = departmentId;
        this.password = password;
        this.userName = userName;
        this.fullName = fullName;
        this.permissionGroupId = permissionTemplate;
    }

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }
}
