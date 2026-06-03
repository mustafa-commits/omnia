package com.sc.demo.repository.chat;

import com.sc.demo.model.chat.ChatToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatTokenRepo extends JpaRepository<ChatToken, Long> {
}
