package com.chat.repository;

import com.chat.entity.Connection;
import com.chat.entity.User;
import com.chat.entity.UserConnection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID> {
    List<UserConnection> findAllByUser(User user);
}
