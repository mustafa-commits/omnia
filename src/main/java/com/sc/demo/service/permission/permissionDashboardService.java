package com.sc.demo.service.permission;

import com.sc.demo.model.permission.Permissions;
import com.sc.demo.repository.employees.AddEmployeesRepo;
import com.sc.demo.repository.permission.permissionEmployeeRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class permissionDashboardService {

    @Autowired
    private permissionEmployeeRepo permissionEmployeeRepo;

    @Autowired
    private TokenService tokenService;

    public Boolean addPermission(String permissionName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        permissionEmployeeRepo.save(new Permissions(permissionName, Long.parseLong(userDashboardId)));
        return true;
    }
}
