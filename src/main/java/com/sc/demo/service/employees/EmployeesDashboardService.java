package com.sc.demo.service.employees;

import com.sc.demo.model.dto.employees.EmployeesResponse;
import com.sc.demo.model.employees.AccessToDashboard;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeesDashboardService {

    @Autowired
    private AddEmployeesRepo addEmployeesRepo;

    public Boolean addEmployees(EmployeesResponse employeesResponse){
        addEmployeesRepo.save(new AccessToDashboard(employeesResponse.phone(), employeesResponse.departmentId(),
                employeesResponse.password(), employeesResponse.userName(), employeesResponse.fullName(),
                employeesResponse.privilegesName(), employeesResponse.createBy()));

        return true;
    }
}
