package com.zenika.mcp_server.service;

import com.zenika.mcp_server.entity.Product;
import com.zenika.mcp_server.model.ProductResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    // The mock dependency. Mockito will create a mock implementation of this interface.
    @Mock
    private ProductRepository productRepository;

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

}