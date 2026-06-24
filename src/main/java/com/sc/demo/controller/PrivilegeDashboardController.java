package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.privilege.PrivilegesName;
import com.sc.demo.service.privilege.PrivilegeDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class PrivilegeDashboardController implements SecuredRestController {

    @Autowired
    private PrivilegeDashboardService privilegeDashboardService;

    // اضافة صلاحية لمستخدمي الداش بورد
    @PostMapping("/V1/api/sc/newEmployeePrivileges")
    public Boolean addPrivileges(@RequestParam PrivilegesName privilegeName,
                                 @RequestParam String userPrivilege,
                                 @RequestHeader(name = "authorization") String token){
        return privilegeDashboardService.addPrivileges(privilegeName, userPrivilege, token);
    }
}
