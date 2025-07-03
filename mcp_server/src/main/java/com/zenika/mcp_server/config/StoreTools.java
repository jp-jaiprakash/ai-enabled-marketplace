package com.zenika.mcp_server.config;

import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.model.UserResponse;
import com.zenika.mcp_server.service.StoreService;
import com.zenika.mcp_server.service.UserService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * === WORKSHOP TASK ===
 *
 * Your task is to expose useful methods from `StoreService` as Tools for AI usage.
 *
 * 1. Inject the `StoreService` bean using constructor injection.
 * 2. Create public methods and annotate them with `@Tool`.
 *    - Define a clear `name` and `description` for each tool.
 *    - Delegate the method to the appropriate `StoreService` method.
 *
 * Tip: These tools will be called by an AI agent, so make sure their purpose is clear.
 */
@Component
public class StoreTools {

    private final StoreService storeService;
    private final UserService userService;

    public StoreTools(StoreService storeService, UserService userService) {
        this.storeService = storeService;
        this.userService = userService;
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Expose a tool that lets the AI search for products by a free-text query.
     *
     * 1. Use the `@Tool` annotation with an appropriate name and description.
     * 2. Accept a `String query` parameter from the caller.
     * 3. Call `storeService.searchProducts(query)` and return the result.
     *
     * Tip: This is useful when the AI needs to recommend or find matching items.
     */
    @Tool(name = "searchProducts", description = "Search for products in the store catalog based on a natural language query. including their ID, name, price, and stock and other details")
    public ProductResponseList searchProducts(String query) {
        return storeService.searchProducts(query);
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Expose a tool to place an order for a product.
     *
     * 1. Annotate the method with `@Tool`.
     * 2. Accept an `OrderRequest` object as input.
     * 3. Call `storeService.placeOrder(orderRequest)` and return the result.
     *
     * Hint: This should only be used after the user confirms a purchase.
     */
    @Tool(name = "placeOrder", description = "Place an order for a given product ID and quantity. Use this when the user confirms they want to buy something.Places an order for a client. Use this after a price has been agreed upon. The 'productId' MUST be the numerical ID of the product, which can be found by calling the searchProducts function.")
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        return storeService.placeOrder(orderRequest);
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Expose a tool to check the status of an order.
     *
     * 1. Accept a `String orderId` parameter (e.g., 1,2,).
     * 2. Call `storeService.getOrderStatus(orderId)` and return the result if found.
     * 3. If no result is found, return a new `OrderResponse` indicating it's not found.
     *
     * Tip: This helps the AI inform users about their order progress.
     */
    @Tool(name = "getOrderStatus", description = "Get the current status of an existing order using its specific order ID (e.g., ORD-ABCD).")
    public Object getOrderStatus(String orderId) {
        return storeService.getOrderStatus(orderId)
                .orElse(new OrderResponse(orderId, "Order with ID '" + orderId + "' was not found."));
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Expose a tool to return all products available in the inventory.
     *
     * 1. Annotate the method with `@Tool`.
     * 2. Call `storeService.getAllItems()` and return the result.
     *
     * Tip: This can be useful when the user wants to browse the full catalog.
     */
    @Tool(name = "getAllItems", description = "Get All the items in the inventory.")
    public ProductResponseList getAllItems() {
        return storeService.getAllItems();
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Expose a tool to retrieve a user's details using their ID.
     *
     * 1. Annotate the method with `@Tool`.
     * 2. Call `userService.validateUser(userId)` and return the result.
     *
     * Tip: This can be useful to validate the buyer before placing an order.
     */
    @Tool(name = "getUser", description = "Get user information by user ID. Returns the user's ID, name, and email.")
    public UserResponse getUser(int userId) {
        return userService.validateUser(userId);
    }

}
