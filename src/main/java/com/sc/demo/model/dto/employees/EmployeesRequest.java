package com.sc.demo.model.dto.employees;

public record EmployeesRequest(
        Long dashboardUserId,
        String PHONE,
        String fullName,
        Long permissionName,
        String userName
) {
}
