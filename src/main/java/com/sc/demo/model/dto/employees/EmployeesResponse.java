package com.sc.demo.model.dto.employees;

public record EmployeesResponse(
        String phone,
        Long departmentId,
        String password,
        String userName,
        String fullName,
        Long groupId
) {
}
