package com.zenika.mcp_server.repository;

import com.zenika.mcp_server.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {}