package com.sc.demo.service.privilege;

import com.sc.demo.model.employees.DashboardUsers;
import com.sc.demo.model.privilege.Permissions;
import com.sc.demo.model.privilege.PrivilegesName;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import com.sc.demo.repository.privilege.PrivilegesEmployeeRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PrivilegeDashboardService {

    @Autowired
    private PrivilegesEmployeeRepo privilegesEmployeeRepo;

    @Autowired
    private AddEmployeesRepo addEmployeesRepo;

    @Autowired
    private TokenService tokenService;

    public Boolean addPrivileges(PrivilegesName privilegeName, String userPrivilege, String token){
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<DashboardUsers> byEmployeeId = addEmployeesRepo.findById(Long.parseLong(employeesId));

        privilegesEmployeeRepo.save(new Permissions(userPrivilege, privilegeName, Long.parseLong(employeesId)));
        return true;
    }
}
