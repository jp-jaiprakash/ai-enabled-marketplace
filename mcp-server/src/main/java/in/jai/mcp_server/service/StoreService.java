package in.jai.mcp_server.service;

import in.jai.mcp_server.CustomerOrder;
import in.jai.mcp_server.entity.Product;
import in.jai.mcp_server.model.OrderRequest;
import in.jai.mcp_server.model.OrderResponse;
import in.jai.mcp_server.model.ProductResponse;
import in.jai.mcp_server.model.ProductResponseList;
import in.jai.mcp_server.repository.OrderRepository;
import in.jai.mcp_server.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
@Transactional
public class StoreService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public StoreService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public ProductResponseList searchProducts(String query) {
        List<ProductResponse> productResponses = productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(p -> new ProductResponse(p.getId().toString(), p.getName(), p.getTargetSellingPrice(), p.getStock()))
                .toList();
        return new ProductResponseList(productResponses);
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Product product = productRepository.findById(Integer.valueOf(orderRequest.productId()))
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStock() < orderRequest.quantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        // Update inventory
        product.setStock(product.getStock() - orderRequest.quantity());
        productRepository.save(product);

        // Create the order
        CustomerOrder order = new CustomerOrder(null, orderRequest.productId(), orderRequest.quantity(), "CONFIRMED");
//        orderRepository.save(order);

        // Simulate a dispatch process
        new Thread(() -> {
            try {
                Thread.sleep(10000); // Wait 10 seconds
                order.setStatus("DISPATCHED");
//                orderRepository.save(order);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }).start();

        return new OrderResponse(order.getId(), order.getStatus());
    }

    public Optional<OrderResponse> getOrderStatus(String orderId) {
        return orderRepository.findById(Integer.valueOf(orderId))
                .map(o -> new OrderResponse(o.getId().toString(), o.getStatus()));
    }

    public ProductResponseList getAllItems() {
        return new ProductResponseList(
            productRepository.findAll()
                .stream()
                .map(p -> new ProductResponse(p.getId().toString(), p.getName(), p.getTargetSellingPrice(), p.getStock()))
                .toList()
        );
    }


// Using Records for concise Data Transfer Objects (DTOs)



}

