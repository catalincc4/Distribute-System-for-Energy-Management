package com.chat.repository;

import com.chat.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, UUID> {
}
