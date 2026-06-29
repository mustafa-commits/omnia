package com.sc.demo.service.permission;

import com.sc.demo.model.dto.permission.PermissionRequest;
import com.sc.demo.model.dto.permission.PermissionTemplateRequest;
import com.sc.demo.model.permission.PermissionGroup;
import com.sc.demo.model.permission.Permissions;
import com.sc.demo.repository.permission.PermissionTemplateRepo;
import com.sc.demo.repository.permission.PermissionRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class permissionDashboardService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PermissionTemplateRepo permissionTemplateRepo;

    @Autowired
    private JdbcClient jdbcClient;

    // اضافة صلاحية على الداش بورد
    public Boolean addPermission(String permissionName, Long groupId, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        permissionRepo.save(new Permissions(permissionName, permissionTemplateRepo.getReferenceById(groupId), Long.parseLong(userDashboardId)));
        return true;
    }

    // اضافة قالب صلاحية على الداش بورد
    public Boolean addPermissionTemplate(String groupName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        permissionTemplateRepo.save(new PermissionGroup(groupName, Long.parseLong(userDashboardId)));
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

    // تعديل قالب الصلاحية
    public Boolean editPermissionTemplate(Long groupId, String groupName, String token){
        var userDashboardId = tokenService.decodeToken(token.substring(7)).getSubject();

        PermissionGroup updatePermissionTemplate = permissionTemplateRepo.findById(groupId).get();
        updatePermissionTemplate.setGroupName(groupName);
        updatePermissionTemplate.setLastUpdate(LocalDateTime.now());
        updatePermissionTemplate.setLastUpdateBy(Long.parseLong(userDashboardId));

        permissionTemplateRepo.save(updatePermissionTemplate);
        return true;
    }

    //  جلب الصلاحيات في الداش بورد
    public List<PermissionRequest> getPermissions(){
        return jdbcClient.sql("""
                SELECT PERMISSION_ID AS permissionId,
                       PERMISSION_NAME AS permissionName
                FROM MOBAPP.SC_DASHBOARD_PERMISSIONS
                ORDER BY PERMISSION_ID
                """)
                .query(PermissionRequest.class)
                .list();
    }

    //  جلب قوالب الصلاحيات في الداش بورد
    public List<PermissionTemplateRequest> getPermissionTemplates(){
        return jdbcClient.sql("""
                SELECT GROUP_ID AS groupId,
                       GROUP_NAME AS groupName
                FROM MOBAPP.SC_DASHBOARD_GROUP_PERMISSIONS
                ORDER BY GROUP_ID
                """)
                .query(PermissionTemplateRequest.class)
                .list();
    }

}
