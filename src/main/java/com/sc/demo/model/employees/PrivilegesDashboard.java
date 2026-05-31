package com.sc.demo.model.employees;

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

    private Long userId;

    private String userPrivilege;

    private String privilegeName;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;

//    @ManyToOne
//    @JoinColumn(name = "USER_ID")
//    private AccessToDashboard accessToDashboard;

    @PrePersist
    public void prePersist(){this.createDate = LocalDateTime.now(); }

}
