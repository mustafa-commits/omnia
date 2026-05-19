package com.sc.demo.repository;

import com.sc.demo.model.chat.AppChatDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatDetailsRepo extends JpaRepository<AppChatDetails, Long> {
}
