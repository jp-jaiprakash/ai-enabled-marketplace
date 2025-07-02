package com.zenika.mcp_server.service;


import com.zenika.mcp_server.model.ProductResponseList;


public interface IStoreService {
    ProductResponseList searchProducts(String query);

    ProductResponseList getAllItems();
}
