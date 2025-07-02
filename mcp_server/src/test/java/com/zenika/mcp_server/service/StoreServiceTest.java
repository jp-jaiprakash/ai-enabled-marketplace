package com.zenika.mcp_server.service;

import com.zenika.mcp_server.entity.Order;
import com.zenika.mcp_server.entity.OrderItem;
import com.zenika.mcp_server.entity.Product;
import com.zenika.mcp_server.entity.User;
import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponse;
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

    // The mock dependency. Mockito will create a mock implementation of this interface.
    @Mock
    private ProductRepository productRepository;

    @Mock private UserRepository userRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;

    // The class we are testing. Mockito will inject the mock objects (productRepository) into this instance.
    @InjectMocks
    private StoreService storeService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create sample data that our mock repository will return
        product1 = new Product();
        product1.setId(1);
        product1.setName("Laptop Pro");
        product1.setDescription("A powerful laptop for professionals.");
        product1.setTargetSellingPrice(1200.00);
        product1.setStock(50);

        product2 = new Product();
        product2.setId(2);
        product2.setName("Wireless Mouse");
        product2.setDescription("An ergonomic wireless mouse.");
        product2.setTargetSellingPrice(75.50);
        product2.setStock(200);
    }

    @Test
    void testGetAllItems_ShouldReturnMappedProducts() {
        // Arrange: Define the behavior of the mock repository.
        // When productRepository.findAll() is called, return our list of sample products.
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act: Call the method we want to test.
        ProductResponseList result = storeService.getAllItems();

        // Assert: Verify the result is as expected.
        assertThat(result).isNotNull();
        assertThat(result.products()).hasSize(2);

        // Verify the first product was mapped correctly
        assertThat(result.products().get(0).id()).isEqualTo(product1.getId());
        assertThat(result.products().get(0).name()).isEqualTo(product1.getName());
        assertThat(result.products().get(0).price()).isEqualTo(product1.getTargetSellingPrice());
        assertThat(result.products().get(0).stock()).isEqualTo(product1.getStock());
        assertThat(result.products().get(0).description()).isEqualTo(product1.getDescription());

        // Verify the second product was mapped correctly
        assertThat(result.products().get(1).id()).isEqualTo(product2.getId());
        assertThat(result.products().get(1).name()).isEqualTo(product2.getName());
    }

    @Test
    void testGetAllItems_ShouldReturnEmptyList_WhenNoProductsExist() {
        // Arrange: Configure the mock to return an empty list.
        when(productRepository.findAll()).thenReturn(List.of());

        // Act: Call the method.
        ProductResponseList result = storeService.getAllItems();

        // Assert: Verify the result is an empty list.
        assertThat(result).isNotNull();
        assertThat(result.products()).isEmpty();
    }

    @Test
    void testSearchProducts_ShouldReturnMatchingProducts() {
        // Arrange
        String query = "laptop";

        // Set up a product that matches the search query
        Product matchingProduct = new Product();
        matchingProduct.setId(3);
        matchingProduct.setName("Gaming Laptop");
        matchingProduct.setDescription("A high-performance gaming laptop.");
        matchingProduct.setTargetSellingPrice(1500.00);
        matchingProduct.setStock(20);

        // Set up another product that also matches
        Product anotherMatchingProduct = new Product();
        anotherMatchingProduct.setId(4);
        anotherMatchingProduct.setName("Laptop Sleeve");
        anotherMatchingProduct.setDescription("Protective sleeve for laptops.");
        anotherMatchingProduct.setTargetSellingPrice(25.00);
        anotherMatchingProduct.setStock(100);

        when(productRepository.findByNameContainingIgnoreCase(query)).thenReturn(List.of(matchingProduct, anotherMatchingProduct));

        // Act
        ProductResponseList result = storeService.searchProducts(query);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.products()).hasSize(2);

        ProductResponse response1 = result.products().get(0);
        assertThat(response1.id()).isEqualTo(matchingProduct.getId());
        assertThat(response1.name()).isEqualTo(matchingProduct.getName());
        assertThat(response1.price()).isEqualTo(matchingProduct.getTargetSellingPrice());
        assertThat(response1.stock()).isEqualTo(matchingProduct.getStock());
        assertThat(response1.description()).isEqualTo(matchingProduct.getDescription());

        ProductResponse response2 = result.products().get(1);
        assertThat(response2.id()).isEqualTo(anotherMatchingProduct.getId());
        assertThat(response2.name()).isEqualTo(anotherMatchingProduct.getName());
    }

    @Test
    void testPlaceOrder_ShouldSucceed_WhenStockIsSufficient() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest("1", 2, 1, 101); // productId = "1", quantity = 2, userId = 101

        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setStock(5);
        product.setTargetSellingPrice(100.0);

        User user = new User();
        user.setId(1);
        user.setName("John");

        // Simulate save setting the ID
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(999); // <-- Important
            return order;
        });

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        OrderResponse response = storeService.placeOrder(orderRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isEqualTo("999");
        assertThat(response.status()).isEqualTo("CONFIRMED");

        verify(productRepository).save(argThat(p -> p.getStock() == 3));
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void testPlaceOrder_ShouldThrowException_WhenNotEnoughStock() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("Mouse");
        product.setStock(1);

        OrderRequest request = new OrderRequest("1", 5, 2, 101);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThatThrownBy(() -> storeService.placeOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock for product: Mouse");

        verify(productRepository, never()).save(any());
        verifyNoInteractions(userRepository, orderRepository, orderItemRepository);
    }

    @Test
    void testGetOrderStatus_ShouldReturnOrderResponse_WhenOrderExists() {
        // Arrange
        Order order = new Order();
        order.setId(123);
        order.setStatus("DISPATCHED");

        when(orderRepository.findById(123)).thenReturn(Optional.of(order));

        // Act
        Optional<OrderResponse> result = storeService.getOrderStatus("123");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().orderId()).isEqualTo("123");
        assertThat(result.get().status()).isEqualTo("DISPATCHED");

        verify(orderRepository).findById(123);
    }

    @Test
    void testGetOrderStatus_ShouldReturnEmpty_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<OrderResponse> result = storeService.getOrderStatus("999");

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository).findById(999);
    }


}