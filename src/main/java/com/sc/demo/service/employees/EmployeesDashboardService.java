package com.sc.demo.service.employees;

import com.sc.demo.model.dto.employees.EmployeesRequest;
import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.model.employees.AccessToDashboard;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesDashboardService {

    @Autowired
    private AddEmployeesRepo addEmployeesRepo;

    @Autowired
    private JdbcClient jdbcClient;

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
}
