package com.sc.demo.repository;

import com.sc.demo.model.chat.AppChatMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepo extends JpaRepository<AppChatMaster, Long> {
}
