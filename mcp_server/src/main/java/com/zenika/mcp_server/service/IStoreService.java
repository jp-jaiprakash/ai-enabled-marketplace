package com.zenika.mcp_server.service;


import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponseList;

import java.util.Optional;

public interface IStoreService {
    ProductResponseList searchProducts(String query);

    OrderResponse placeOrder(OrderRequest orderRequest);

    Optional<OrderResponse> getOrderStatus(String orderId);

    ProductResponseList getAllItems();
}
