package com.sc.demo.repository.userDashboard;

import com.sc.demo.model.userDashboard.UserDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddUserDashboardRepo extends JpaRepository<UserDashboard, Long> {
}
