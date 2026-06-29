package com.sc.demo.service.userDashboard;

import com.sc.demo.model.dto.permission.DashboardPermissionsRequest;
import com.sc.demo.model.dto.usersDashboard.UsersDashboardRequest;
import com.sc.demo.model.dto.usersDashboard.UserDashboardResponse;
import com.sc.demo.model.dto.login.LoginRequest;
import com.sc.demo.model.dto.login.LoginRequest2;
import com.sc.demo.model.dto.permission.PermissionRequest;
import com.sc.demo.model.userDashboard.UserDashboard;
import com.sc.demo.repository.permission.PermissionTemplateRepo;
import com.sc.demo.repository.userDashboard.AddUserDashboardRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsersDashboardService {

    @Autowired
    private AddUserDashboardRepo addUserDashboardRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PermissionTemplateRepo permissionTemplateRepo;

    public Boolean newUserDashboard(UserDashboardResponse userDashboardResponse, String token) {
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        addUserDashboardRepo.save(new UserDashboard(userDashboardResponse.phone(), userDashboardResponse.departmentId(),
                userDashboardResponse.password(), userDashboardResponse.userName(), userDashboardResponse.fullName(),
                permissionTemplateRepo.getReferenceById(userDashboardResponse.groupId()), Long.parseLong(userDashboardId)));

        return true;
    }

    // جلب جميع الصلاحيات
    public List<DashboardPermissionsRequest> viewDashboardPermissions() {
        return jdbcClient.sql("""
                        SELECT GP.GROUP_ID AS groupId,
                               GROUP_NAME AS groupName,
                               GP.IS_ACTIVE AS isActive,
                               PERMISSION_NAME AS permissionName
                        FROM MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP
                        LEFT JOIN MOBAPP.SC_DASHBOARD_PERMISSIONS P ON P.GROUP_ID = GP.GROUP_ID
                        """)
                .query(DashboardPermissionsRequest.class)
                .list();
    }

    // جميع مستخدمي الداش بورد
    public List<UsersDashboardRequest> viewUsersDashboard() {
        return jdbcClient.sql("""
                        SELECT USER_ID AS userId,
                               PHONE,
                               FULL_NAME AS fullName,
                               USER_NAME AS userName,
                               GROUP_NAME AS groupName
                        FROM MOBAPP.SC_DASHBOARD_USER DU
                        LEFT JOIN MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP ON DU.GROUP_ID = GP.GROUP_ID
                        ORDER BY USER_ID
                        """)
                .query(UsersDashboardRequest.class)
                .list();
    }

    // التحقق من مستخدم الداش بورد
    public ResponseEntity<LoginRequest2> loginUserDashboard(String userName, String password) {
        Optional<LoginRequest> dashboardLoginCheck = jdbcClient.sql("""
                        SELECT DU.USER_ID AS userId
                              ,USER_NAME AS userName
                              ,GP.GROUP_ID AS groupId
                              ,GROUP_NAME AS groupName
                        FROM MOBAPP.SC_DASHBOARD_USER DU
                        JOIN MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP ON DU.GROUP_ID = GP.GROUP_ID
                        WHERE USER_NAME = :userName
                        AND PASSWORD = :password
                        AND DU.IS_ACTIVE = 1 AND GP.IS_ACTIVE = 1
                        """)
                .param("userName", userName)
                .param("password", password)
                .query(LoginRequest.class)
                .optional();

        if (dashboardLoginCheck.isPresent()){
        List<PermissionRequest> dashboardPermission = jdbcClient.sql("""
                        SELECT P.PERMISSION_ID AS permissionId,
                               PERMISSION_NAME AS permissionName
                        FROM MOBAPP.SC_DASHBOARD_PERMISSIONS P
                        JOIN MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP ON GP.GROUP_ID = P.GROUP_ID
                        WHERE P.GROUP_ID = :groupId
                        """)
                .param("groupId", dashboardLoginCheck.get().groupId())
                .query(PermissionRequest.class)
                .list();

            if (dashboardLoginCheck.isPresent()) {
                return ResponseEntity.ok(new LoginRequest2(
                        dashboardLoginCheck.get().userId(),
                        dashboardLoginCheck.get().userName(),
                        dashboardLoginCheck.get().groupId(),
                        dashboardLoginCheck.get().groupName(),
                        dashboardPermission,
                        tokenService.generateUserDashboardToken(
                                String.valueOf(dashboardLoginCheck.get().userId()),
                                dashboardLoginCheck.get().userName(),
                                Long.parseLong(String.valueOf(dashboardLoginCheck.get().groupId()))
                        )));
            }
        }
        return null;
    }



}
