package com.zenika.mcp_server.repository;

import com.zenika.mcp_server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByName(String name);

    List<Product> findByDescription(String description);

    Optional<Product> findByNameContainingIgnoreCase(String query);
}