package com.sc.demo.repository.chat;

import com.sc.demo.model.Tokens.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatTokenRepo extends JpaRepository<AppToken, Long> {
}
