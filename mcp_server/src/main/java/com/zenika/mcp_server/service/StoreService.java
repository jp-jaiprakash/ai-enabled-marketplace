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


@Service
@Transactional
public class StoreService implements IStoreService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    public StoreService(ProductRepository productRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ProductResponseList searchProducts(String query) {
        List<ProductResponse> productResponses = productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getTargetSellingPrice(), p.getStock(), p.getDescription())
                )
                .toList();
        return new ProductResponseList(productResponses);
    }

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

    @Override
    public Optional<OrderResponse> getOrderStatus(String orderId) {
        return orderRepository.findById(Integer.valueOf(orderId))
                .map(o -> new OrderResponse(o.getId().toString(), o.getStatus()));
    }

    public ProductResponseList getAllItems() {
        return new ProductResponseList(
                productRepository.findAll()
                        .stream()
                        .map(p -> new ProductResponse(p.getId(), p.getName(), p.getTargetSellingPrice(), p.getStock(), p.getDescription()))
                        .toList()
        );
    }


// Using Records for concise Data Transfer Objects (DTOs)


}

