package com.sc.demo.repository.privilege;

import com.sc.demo.model.privilege.PrivilegesDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegesEmployeeRepo extends JpaRepository<PrivilegesDashboard, Long> {
}
