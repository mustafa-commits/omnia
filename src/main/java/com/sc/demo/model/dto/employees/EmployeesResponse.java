package com.sc.demo.model.dto.employees;

import com.sc.demo.model.employees.Department;
import com.sc.demo.model.employees.PrivilegesName;

public record EmployeesResponse(
        String phone,
        Department departmentId,
        String password,
        String userName,
        PrivilegesName privilegesName,
        Long createBy
) {
}
