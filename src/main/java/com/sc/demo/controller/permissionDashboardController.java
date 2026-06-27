package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.service.permission.permissionDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class permissionDashboardController implements SecuredRestController {

    @Autowired
    private permissionDashboardService permissionDashboardService;

    // اضافة صلاحية لمستخدمي الداش بورد
    @PostMapping("/V1/api/sc/newEmployeepermission")
    public Boolean addpermission(@RequestParam String permissionName,
                                 @RequestParam String userpermission,
                                 @RequestHeader(name = "authorization") String token){
        return permissionDashboardService.addpermission(permissionName, userpermission, token);
    }
}
