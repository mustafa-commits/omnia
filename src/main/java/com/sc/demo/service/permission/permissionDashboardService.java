package com.sc.demo.service.permission;

import com.sc.demo.model.employees.DashboardUsers;
import com.sc.demo.model.permission.Permissions;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import com.sc.demo.repository.permission.permissionEmployeeRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class permissionDashboardService {

    @Autowired
    private permissionEmployeeRepo permissionEmployeeRepo;

    @Autowired
    private AddEmployeesRepo addEmployeesRepo;

    @Autowired
    private TokenService tokenService;

    public Boolean addpermission(String permissionName, String userpermission, String token){
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();
        Optional<DashboardUsers> byEmployeeId = addEmployeesRepo.findById(Long.parseLong(employeesId));

        permissionEmployeeRepo.save(new Permissions(userpermission, permissionName, Long.parseLong(employeesId)));
        return true;
    }
}
