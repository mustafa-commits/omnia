package com.sc.demo.model.dto.employees;

import com.sc.demo.model.privilege.PrivilegesName;

public record EmployeesResponse(
        String phone,
        Long departmentId,
        String password,
        String userName,
        String fullName,
        PrivilegesName privilegesName,
        Long createBy
) {
}
