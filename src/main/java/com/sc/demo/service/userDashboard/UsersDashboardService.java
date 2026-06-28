package com.sc.demo.service.userDashboard;

import com.sc.demo.model.dto.DashboardUser.DashboardUserRequest;
import com.sc.demo.model.dto.DashboardUser.UserDashboardResponse;
import com.sc.demo.model.dto.login.LoginRequest;
import com.sc.demo.model.dto.permission.PermissionRequest;
import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.model.userDashboard.UserDashboard;
import com.sc.demo.repository.userDashboard.AddUserDashboardRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Boolean newUserDashboard(UserDashboardResponse userDashboardResponse, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        addUserDashboardRepo.save(new UserDashboard(userDashboardResponse.phone(), userDashboardResponse.departmentId(),
                userDashboardResponse.password(), userDashboardResponse.userName(), userDashboardResponse.fullName(),
                userDashboardResponse.permissionTemplate(), Long.parseLong(userDashboardId)));

        return true;
    }

    // جميع مستخدمي الداش بورد
    public List<DashboardUserRequest> viewDashboardUser(){
        return jdbcClient.sql("""
                SELECT USER_DASHBOARD_ID AS userDashboardId,
                       PHONE,
                       FULL_NAME AS fullName,
                       USER_NAME AS userName
                FROM MOBAPP.SC_DASHBOARD_USER
                ORDER BY USER_DASHBOARD_ID
                """)
                .query(DashboardUserRequest.class)
                .list();
    }

    // التحقق من مستخدم الداش بورد
    public LoginRequest loginUserDashboard(String userName, String password){
        Optional<LoginRequest> dashboardLoginCheck = jdbcClient.sql("""
                SELECT USER_DASHBOARD_ID AS userId
                      ,USER_NAME AS userName
                      ,GROUP_ID AS groupId
                FROM MOBAPP.SC_DASHBOARD_USER DU
                LEFT JOIN MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP ON DU.USER_DASHBOARD_ID = GP.USER_DASHBOARD
                WHERE USER_NAME = :userName
                AND PASSWORD = :password
                """)
                .param("userName", userName)
                .param("password", password)
                .query(LoginRequest.class)
                .optional(); // x record userId , name , groupId

        //new query to get list of permissions
        // class { x record, list<Int
        List<PermissionRequest> dashboardPermission = jdbcClient.sql("""
                SELECT P.PERMISSION_ID AS permissionId,
                       PERMISSION_NAME AS permissionName
                FROM MOBAPP.SC_DASHBOARD_PERMISSIONS P
                LEFT JOIN MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS GP ON GP.PERMISSION_ID = P.PERMISSION_ID
                LEFT JOIN MOBAPP.SC_DASHBOARD_USER DU ON DU.USER_ID = GP.USER_ID
                WHERE DU.USER_ID = :userId
                """)
                .param("userId", dashboardLoginCheck.get().userId())
                .query(PermissionRequest.class)
                .list();

        if (dashboardLoginCheck.isPresent()) {
            return new LoginRequest(dashboardLoginCheck.get().userId(), dashboardLoginCheck.get().userName(), dashboardLoginCheck.get().groupId(),
                    dashboardPermission.get().permissionId(), dashboardPermission.get().permissionName(),
                    tokenService.generateUserDashboardToken(String.valueOf(dashboardLoginCheck.get().userId()), dashboardLoginCheck.get().userName()
                            , Long.parseLong(String.valueOf(dashboardLoginCheck.get().groupId()))));
        }
        return null;
    }

}
