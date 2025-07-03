package com.zenika.mcp_server.service;

import com.zenika.mcp_server.entity.Order;
import com.zenika.mcp_server.entity.OrderItem;
import com.zenika.mcp_server.entity.Product;
import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.repository.OrderItemRepository;
import com.zenika.mcp_server.repository.OrderRepository;
import com.zenika.mcp_server.repository.ProductRepository;
import com.zenika.mcp_server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * === WORKSHOP TASK ===
 *
 * This service class is responsible for handling store operations.
 *
 * TASKS:
 * 1. Inject all required repositories via constructor injection.
 * 2. Implement core business logic for searching products, placing orders, checking order status, and retrieving all items.
 * 3. Use appropriate error handling and validation.
 * 4. Follow clean coding practices — keep methods concise and focused.
 */
@Service
@Transactional
public class StoreService implements IStoreService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public StoreService(ProductRepository productRepository,
                        UserRepository userRepository,
                        OrderItemRepository orderItemRepository,
                        OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Implement a search function that retrieves products matching a text query.
     *
     * 1. Use the `productRepository.findByNameContainingIgnoreCase()` method to fetch relevant products.
     * 2. Map each product to a `ProductResponse` DTO using Java Streams.
     * 3. Wrap the result in a `ProductResponseList` and return.
     *
     * Bonus: Think about how this could be extended for fuzzy or advanced search in future.
     */
    @Override
    public ProductResponseList searchProducts(String query) {
        List<ProductResponse> productResponses = productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getTargetSellingPrice(),
                        p.getStock(),
                        p.getDescription()))
                .toList();
        return new ProductResponseList(productResponses);
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Implement the logic to place an order.
     *
     * 1. Validate the product exists and has enough stock.
     * 2. Validate the user placing the order exists.
     * 3. Deduct stock from the product and save it.
     * 4. Create a new `Order` with the correct total amount and status.
     * 5. Create an associated `OrderItem` with quantity and price details.
     * 6. Use a background thread to simulate dispatching the order after a delay.
     * 7. Return an `OrderResponse` with the order ID and initial status.
     *
     * Bonus: Consider what failure scenarios you should handle (e.g., missing product/user, invalid quantity).
     */
    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        // Retrieve product
        Product product = productRepository.findById(Integer.valueOf(orderRequest.productId()))
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Validate stock
        if (product.getStock() < orderRequest.quantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        // Retrieve user
        var user = userRepository.findById(orderRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Deduct stock
        product.setStock(product.getStock() - orderRequest.quantity());
        productRepository.save(product);

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("CONFIRMED");
        order.setTotalAmount(orderRequest.negotiatedSellingPrice() * orderRequest.quantity());
        order.setOrderDate(java.time.LocalDateTime.now());
        orderRepository.save(order);

        // Create OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderRequest.quantity());
        orderItem.setPrice(orderRequest.negotiatedSellingPrice());
        orderItemRepository.save(orderItem);

        // Simulate dispatch process
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Wait 1 second
                order.setStatus("DISPATCHED");
                orderRepository.save(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return new OrderResponse(order.getId().toString(), order.getStatus());
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Implement a method to fetch the current status of an order.
     *
     * 1. Accept the order ID as a string.
     * 2. Retrieve the `Order` entity by its ID using the `orderRepository`.
     * 3. Map it to an `OrderResponse` containing ID and status.
     * 4. Return `Optional.empty()` if no order is found.
     *
     * Tip: Handle `NumberFormatException` if the input ID is not a valid number.
     */
    @Override
    public Optional<OrderResponse> getOrderStatus(String orderId) {
        return orderRepository.findById(Integer.valueOf(orderId))
                .map(o -> new OrderResponse(o.getId().toString(), o.getStatus()));
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Implement a simple function to return all products from the catalog.
     *
     * 1. Fetch all `Product` entities using `productRepository.findAll()`.
     * 2. Map them to `ProductResponse` DTOs.
     * 3. Wrap the result in a `ProductResponseList` and return.
     *
     * Bonus: Add sorting/filtering in the future based on stock or price.
     */
    public ProductResponseList getAllItems() {
        return new ProductResponseList(
                productRepository.findAll()
                        .stream()
                        .map(p -> new ProductResponse(
                                p.getId(),
                                p.getName(),
                                p.getTargetSellingPrice(),
                                p.getStock(),
                                p.getDescription()))
                        .toList()
        );
    }

}
