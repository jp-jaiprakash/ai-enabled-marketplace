package com.zenika.mcp_server.model;

public record OrderRequest(String productId, int quantity, int userId, double negotiatedSellingPrice) {}