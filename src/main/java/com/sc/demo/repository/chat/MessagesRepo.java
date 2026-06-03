package com.sc.demo.repository.chat;

import com.sc.demo.model.chat.AppChatDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepo extends JpaRepository<AppChatDetails, Long> {
}
