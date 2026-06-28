package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.permission.PermissionRequest;
import com.sc.demo.model.dto.permission.PermissionTemplateRequest;
import com.sc.demo.model.permission.PermissionGroup;
import com.sc.demo.service.permission.permissionDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class permissionDashboardController implements SecuredRestController {

    @Autowired
    private permissionDashboardService permissionDashboardService;

    // اضافة صلاحية على الداش بورد
    @PostMapping("/V1/api/sc/addPermission")
    public Boolean addPermission(@RequestParam String permissionName,
                                 @RequestParam Long groupId,
                                 @RequestHeader(name = "authorization") String token){
        return permissionDashboardService.addPermission(permissionName, groupId, token);
    }

    // اضافة قالب للصلاحيات على الداش بورد
    @PostMapping("/V1/api/sc/addPermissionTemplate")
    public Boolean addPermissionTemplate(@RequestParam String groupName,
                                         @RequestHeader(name = "authorization") String token){
        return permissionDashboardService.addPermissionTemplate(groupName, token);
    }

    // تعديل  اسم الصلاحية على الداش بورد
    @PutMapping("/V1/api/sc/editPermission")
    public Boolean editPermission(@RequestParam Long permissionId,
                                  @RequestParam String permissionName,
                                  @RequestHeader(name = "authorization") String token){
        return permissionDashboardService.editPermission(permissionId, permissionName, token);
    }

    // تعديل  اسم قالب الصلاحية على الداش بورد
    @PutMapping("/V1/api/sc/editPermissionTemplate")
    public Boolean editPermissionTemplate(@RequestParam Long groupId,
                                          @RequestParam String groupName,
                                          @RequestHeader(name = "authorization") String token){
        return permissionDashboardService.editPermissionTemplate(groupId, groupName, token);
    }

    //  جلب الصلاحيات في الداش بورد
    @GetMapping("/V1/api/sc/getPermissions")
    public List<PermissionRequest> getPermissions(){
        return permissionDashboardService.getPermissions();
    }

    //  جلب قالب الصلاحيات في الداش بورد
    @GetMapping("/V1/api/sc/getPermissionTemplates")
    public List<PermissionTemplateRequest> getPermissionTemplates(){
        return permissionDashboardService.getPermissionTemplates();
    }
}
