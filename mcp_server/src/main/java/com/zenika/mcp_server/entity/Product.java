package com.zenika.mcp_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Double minimumSellingPrice;

    private Double targetSellingPrice;

    private Integer stock;

    private Integer estimateDeliveryDays;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Product() {
    }

    public Product(Integer id, String name, String description, Double minimumSellingPrice, Double targetSellingPrice, Integer stock, Integer estimateDeliveryDays, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minimumSellingPrice = minimumSellingPrice;
        this.targetSellingPrice = targetSellingPrice;
        this.stock = stock;
        this.estimateDeliveryDays = estimateDeliveryDays;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMinimumSellingPrice() {
        return minimumSellingPrice;
    }

    public void setMinimumSellingPrice(Double minimumSellingPrice) {
        this.minimumSellingPrice = minimumSellingPrice;
    }

    public Double getTargetSellingPrice() {
        return targetSellingPrice;
    }

    public void setTargetSellingPrice(Double targetSellingPrice) {
        this.targetSellingPrice = targetSellingPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getEstimateDeliveryDays() {
        return estimateDeliveryDays;
    }

    public void setEstimateDeliveryDays(Integer estimateDeliveryDays) {
        this.estimateDeliveryDays = estimateDeliveryDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
