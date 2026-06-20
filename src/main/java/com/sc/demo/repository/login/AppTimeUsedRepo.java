package com.sc.demo.repository.login;

import com.sc.demo.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppTimeUsedRepo extends JpaRepository<AppUser, LocalDateTime> {
}
