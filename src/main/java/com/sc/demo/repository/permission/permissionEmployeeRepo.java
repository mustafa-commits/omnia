package com.sc.demo.repository.permission;

import com.sc.demo.model.permission.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface permissionEmployeeRepo extends JpaRepository<Permissions, Long> {
}
