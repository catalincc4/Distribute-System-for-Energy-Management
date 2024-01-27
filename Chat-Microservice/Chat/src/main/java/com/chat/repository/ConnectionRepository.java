package com.chat.repository;

import com.chat.entity.Connection;
import com.chat.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface ConnectionRepository extends CrudRepository<Connection, UUID> {

    List<Connection> findAllByAdminUserOrAdminUserNull(User user);
}