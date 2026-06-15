package com.sc.demo.service.employees;

import com.sc.demo.model.dto.employees.EmployeesRequest;
import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.model.dto.login.GetDashboardUserIdWithToken;
import com.sc.demo.model.dto.login.GetUserIdWithToken;
import com.sc.demo.model.employees.AccessToDashboard;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeesDashboardService {

    @Autowired
    private AddEmployeesRepo addEmployeesRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private TokenService tokenService;

    public Boolean addEmployees(EmployeesResponse employeesResponse){
        addEmployeesRepo.save(new AccessToDashboard(employeesResponse.phone(), employeesResponse.departmentId(),
                employeesResponse.password(), employeesResponse.userName(), employeesResponse.fullName(),
                employeesResponse.privilegesName(), employeesResponse.createBy()));

        return true;
    }

    public List<EmployeesRequest> viewEmployees(){
        return jdbcClient.sql("""
                SELECT DASHBOARD_USER_ID AS dashboardUserId,
                       PHONE,
                       FULL_NAME AS fullName,
                       PRIVILEGES_NAME AS privilegesName,
                       USER_NAME AS userName
                FROM MOBAPP.SC_DASHBOARD_USERS
                """)
                .query(EmployeesRequest.class)
                .list();
    }

    public GetUserIdWithToken checkedEmployee(String userName, String password){
        Optional<AccessToDashboard> dashboardCheck = jdbcClient.sql("""
                SELECT DASHBOARD_USER_ID AS dashboardUserId
                FROM MOBAPP.SC_DASHBOARD_USERS
                WHERE USER_NAME = :userName
                AND PASSWORD = :password
                """)
                .param("userName", userName)
                .param("password", password)
                .query(AccessToDashboard.class)
                .optional();

        if (dashboardCheck.isPresent()) {
            return new GetUserIdWithToken(dashboardCheck.get().getDashboardUserId(),
                    tokenService.generateEmployeesToken(String.valueOf(dashboardCheck.get().getDashboardUserId())));
        }
        return null;
    }
}
