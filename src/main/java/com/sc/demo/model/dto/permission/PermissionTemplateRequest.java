package com.sc.demo.model.dto.permission;

public record PermissionTemplateRequest(
        Long groupId,
        String permissionTemplateName
) {
}
