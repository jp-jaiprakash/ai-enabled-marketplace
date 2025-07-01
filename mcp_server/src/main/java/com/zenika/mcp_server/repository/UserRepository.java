package com.zenika.mcp_server.repository;

import com.zenika.mcp_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}



