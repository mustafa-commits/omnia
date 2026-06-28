package com.sc.demo.service.userDashboard;

import com.sc.demo.model.dto.DashboardUser.DashboardUserRequest;
import com.sc.demo.model.dto.DashboardUser.UserDashboardResponse;
import com.sc.demo.model.userDashboard.UserDashboard;
import com.sc.demo.model.dto.token.TokenRequest;
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
                userDashboardResponse.password(), userDashboardResponse.userName(), userDashboardResponse.fullName(), Long.parseLong(userDashboardId)));

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
    public TokenRequest loginUserDashboard(String userName, String password){
        Optional<UserDashboard> dashboardCheck = jdbcClient.sql("""
                SELECT USER_DASHBOARD_ID AS userDashboardId
                FROM MOBAPP.SC_DASHBOARD_USER
                WHERE USER_NAME = :userName
                AND PASSWORD = :password
                """)
                .param("userName", userName)
                .param("password", password)
                .query(UserDashboard.class)
                .optional();

        if (dashboardCheck.isPresent()) {
            return new TokenRequest(dashboardCheck.get().getUserDashboardId(),
                    tokenService.generateUserDashboardToken(String.valueOf(dashboardCheck.get().getUserDashboardId())));
        }
        return null;
    }
}
