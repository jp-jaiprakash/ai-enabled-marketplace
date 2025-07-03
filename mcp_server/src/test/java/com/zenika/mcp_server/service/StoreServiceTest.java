package com.zenika.mcp_server.service;

import com.zenika.mcp_server.entity.Order;
import com.zenika.mcp_server.entity.OrderItem;
import com.zenika.mcp_server.entity.Product;
import com.zenika.mcp_server.entity.User;
import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.repository.OrderItemRepository;
import com.zenika.mcp_server.repository.OrderRepository;
import com.zenika.mcp_server.repository.ProductRepository;
import com.zenika.mcp_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks
    private StoreService storeService;

    private Product product1;
    private Product product2;
    private Product gamingLaptop;
    private Product laptopSleeve;
    private User user;
    private Order order;
    private OrderRequest validOrderRequest;
    private OrderRequest insufficientStockRequest;

    @BeforeEach
    void setUp() {
        product1 = createProduct(1, "Laptop Pro", "A powerful laptop for professionals.", 1200.00, 50);
        product2 = createProduct(2, "Wireless Mouse", "An ergonomic wireless mouse.", 75.50, 200);

        gamingLaptop = createProduct(3, "Gaming Laptop", "A high-performance gaming laptop.", 1500.00, 20);
        laptopSleeve = createProduct(4, "Laptop Sleeve", "Protective sleeve for laptops.", 25.00, 100);

        user = new User();
        user.setId(1);
        user.setName("John");

        order = new Order();
        order.setId(123);
        order.setStatus("DISPATCHED");

        validOrderRequest = new OrderRequest("1", 2, 1, 101);
        insufficientStockRequest = new OrderRequest("1", 5, 2, 101);
    }

    private Product createProduct(int id, String name, String description, double price, int stock) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setDescription(description);
        p.setTargetSellingPrice(price);
        p.setStock(stock);
        return p;
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test the `getAllItems()` method in `StoreService`.
     *
     * 1. Mock the repository to return 2 products.
     * 2. Assert that the returned list has the expected names and size.
     */
    @Test
    void testGetAllItems_ShouldReturnMappedProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        ProductResponseList result = storeService.getAllItems();

        assertThat(result.products()).hasSize(2);
        assertThat(result.products().get(0).name()).isEqualTo("Laptop Pro");
        assertThat(result.products().get(1).name()).isEqualTo("Wireless Mouse");
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test `getAllItems()` when no products exist.
     *
     * 1. Mock the repository to return an empty list.
     * 2. Assert that the result is empty.
     */
    @Test
    void testGetAllItems_ShouldReturnEmptyList_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        ProductResponseList result = storeService.getAllItems();

        assertThat(result.products()).isEmpty();
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test product search functionality.
     *
     * 1. Mock the repository to return products matching a query like "laptop".
     * 2. Assert the names and count in the response.
     */
    @Test
    void testSearchProducts_ShouldReturnMatchingProducts() {
        String query = "laptop";
        when(productRepository.findByNameContainingIgnoreCase(query))
                .thenReturn(List.of(gamingLaptop, laptopSleeve));

        ProductResponseList result = storeService.searchProducts(query);

        assertThat(result.products()).hasSize(2);
        assertThat(result.products().get(0).name()).isEqualTo("Gaming Laptop");
        assertThat(result.products().get(1).name()).isEqualTo("Laptop Sleeve");
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test order placement success case.
     *
     * 1. Mock product and user fetch.
     * 2. Simulate saving an order and order item.
     * 3. Assert that the order status and ID match expectations.
     */
    @Test
    void testPlaceOrder_ShouldSucceed_WhenStockIsSufficient() {
        Product orderProduct = createProduct(1, "Laptop", "Fast laptop", 100.0, 5);

        when(productRepository.findById(1)).thenReturn(Optional.of(orderProduct));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(999);
            return o;
        });

        OrderResponse response = storeService.placeOrder(validOrderRequest);

        assertThat(response.orderId()).isEqualTo("999");
        assertThat(response.status()).isEqualTo("CONFIRMED");

        verify(productRepository).save(argThat(p -> p.getStock() == 3));
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test order placement when stock is insufficient.
     *
     * 1. Mock the product with low stock.
     * 2. Assert that the method throws an `IllegalArgumentException`.
     * 3. Ensure no other repositories are called.
     */
    @Test
    void testPlaceOrder_ShouldThrowException_WhenNotEnoughStock() {
        Product lowStockProduct = createProduct(1, "Mouse", "Basic mouse", 25.0, 1);
        when(productRepository.findById(1)).thenReturn(Optional.of(lowStockProduct));

        assertThatThrownBy(() -> storeService.placeOrder(insufficientStockRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock for product: Mouse");

        verifyNoInteractions(userRepository, orderRepository, orderItemRepository);
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test getting order status when order exists.
     *
     * 1. Mock order repository to return an order.
     * 2. Assert that returned Optional contains correct status and ID.
     */
    @Test
    void testGetOrderStatus_ShouldReturnOrderResponse_WhenOrderExists() {
        when(orderRepository.findById(123)).thenReturn(Optional.of(order));

        Optional<OrderResponse> result = storeService.getOrderStatus("123");

        assertThat(result).isPresent();
        assertThat(result.get().orderId()).isEqualTo("123");
        assertThat(result.get().status()).isEqualTo("DISPATCHED");
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Test getting order status when order ID does not exist.
     *
     * 1. Mock repository to return empty Optional.
     * 2. Assert the result is empty.
     */
    @Test
    void testGetOrderStatus_ShouldReturnEmpty_WhenOrderNotFound() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        Optional<OrderResponse> result = storeService.getOrderStatus("999");

        assertThat(result).isEmpty();
    }
}
