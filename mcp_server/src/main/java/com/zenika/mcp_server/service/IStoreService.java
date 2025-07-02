package com.zenika.mcp_server.service;


import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponseList;


public interface IStoreService {
    ProductResponseList searchProducts(String query);

    OrderResponse placeOrder(OrderRequest orderRequest);

    ProductResponseList getAllItems();
}
