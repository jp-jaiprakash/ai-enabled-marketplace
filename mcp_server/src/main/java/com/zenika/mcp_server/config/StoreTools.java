package com.zenika.mcp_server.config;

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


    @Tool(name = "getAllItems", description = "Get All the items in the inventory.")
    public ProductResponseList getAllItems() {
        return storeService.getAllItems();
    }


}