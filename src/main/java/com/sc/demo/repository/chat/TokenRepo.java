package com.sc.demo.repository.chat;

import com.sc.demo.model.token.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<AppToken, Long> {
    Optional<AppToken> findByUserId(Long userId);
}
