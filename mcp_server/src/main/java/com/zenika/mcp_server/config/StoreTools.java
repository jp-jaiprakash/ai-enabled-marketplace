package com.zenika.mcp_server.config;

import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.service.StoreService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class StoreTools {

    private final StoreService storeService;

    public StoreTools(StoreService storeService) {
        this.storeService = storeService;
    }

    @Tool(name = "searchProducts", description = "Search for products in the store catalog based on a natural language query. including their ID, name, price, and stock and other details")
    public ProductResponseList searchProducts(String query) {
        return storeService.searchProducts(query);
    }

    @Tool(name = "placeOrder", description = "Place an order for a given product ID and quantity. Use this when the user confirms they want to buy something.Places an order for a client. Use this after a price has been agreed upon. The 'productId' MUST be the numerical ID of the product, which can be found by calling the searchProducts function.")
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        return storeService.placeOrder(orderRequest);
    }

    @Tool(name = "getOrderStatus", description = "Get the current status of an existing order using its specific order ID (e.g., ORD-ABCD).")
    public Object getOrderStatus(String orderId) {
        return storeService.getOrderStatus(orderId)
                .orElse(new OrderResponse(orderId, "Order with ID '" + orderId + "' was not found."));
    }

    @Tool(name = "getAllItems", description = "Get All the items in the inventory.")
    public ProductResponseList getAllItems() {
        return storeService.getAllItems();
    }


}