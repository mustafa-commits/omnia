package com.sc.demo.repository.employees;

import com.sc.demo.model.employees.DashboardUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddEmployeesRepo extends JpaRepository<DashboardUsers, Long> {
}
