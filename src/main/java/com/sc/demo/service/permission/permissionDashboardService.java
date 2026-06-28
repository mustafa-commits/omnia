package com.sc.demo.service.permission;

import com.sc.demo.model.announcements.Pin;
import com.sc.demo.model.permission.PermissionGroup;
import com.sc.demo.model.permission.Permissions;
import com.sc.demo.repository.permission.PermissionTemplateRepo;
import com.sc.demo.repository.permission.PermissionRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class permissionDashboardService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PermissionTemplateRepo permissionTemplateRepo;

    // اضافة صلاحية على الداش بورد
    public Boolean addPermission(String permissionName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        permissionRepo.save(new Permissions(permissionName, Long.parseLong(userDashboardId)));
        return true;
    }

    // اضافة قالب للصلاحيات على الداش بورد
    public Boolean addPermissionTemplate(String permissionTemplateName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        permissionTemplateRepo.save(new PermissionGroup(permissionTemplateName, Long.parseLong(userDashboardId)));
        return true;
    }

    // تعديل اسم الصلاحية
    public Boolean editPermission(Long permissionId, String permissionName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        Permissions updatePermission = permissionRepo.findById(permissionId).get();
        updatePermission.setPermissionName(permissionName);
        updatePermission.setLastUpdate(LocalDateTime.now());
        updatePermission.setLastUpdateBy(Long.parseLong(userDashboardId));

        permissionRepo.save(updatePermission);
        return true;
    }

    // تعديل اسم قالب الصلاحية
    public Boolean editPermissionTemplate(Long groupId, String permissionTemplateName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        PermissionGroup updatePermissionTemplate = permissionTemplateRepo.findById(groupId).get();
        updatePermissionTemplate.setPermissionTemplateName(permissionTemplateName);
        updatePermissionTemplate.setLastUpdate(LocalDateTime.now());
        updatePermissionTemplate.setLastUpdateBy(Long.parseLong(userDashboardId));

        permissionTemplateRepo.save(updatePermissionTemplate);
        return true;
    }
}
