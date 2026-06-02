package com.sc.demo.repository.chat;

import com.sc.demo.model.chat.appChatDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepo extends JpaRepository<appChatDetails, Long> {
}
