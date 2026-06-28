package com.sc.demo.repository.permission;

import com.sc.demo.model.permission.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionTemplateRepo extends JpaRepository<PermissionGroup, Long> {
}
