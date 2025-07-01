package com.zenika.mcp_server.model;

public record ProductResponse(Integer id, String name, double price, int stock, String description) {}