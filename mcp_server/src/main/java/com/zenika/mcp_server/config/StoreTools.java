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


    @Tool(name = "getAllItems", description = "Get All the items in the inventory.")
    public ProductResponseList getAllItems() {
        return storeService.getAllItems();
    }


}