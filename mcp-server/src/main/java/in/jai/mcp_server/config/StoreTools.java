package in.jai.mcp_server.config;

import in.jai.mcp_server.model.OrderRequest;
import in.jai.mcp_server.model.OrderResponse;
import in.jai.mcp_server.model.ProductResponseList;
import in.jai.mcp_server.service.StoreService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class StoreTools {

    private final StoreService storeService;

    public StoreTools(StoreService storeService) {
        this.storeService = storeService;
    }

    @Tool(name = "searchProducts", description = "Search for products in the store catalog based on a natural language query.")
    public ProductResponseList searchProducts(String query) {
        return storeService.searchProducts(query);
    }

    @Tool(name = "placeOrder", description = "Place an order for a given product ID and quantity. Use this when the user confirms they want to buy something.")
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