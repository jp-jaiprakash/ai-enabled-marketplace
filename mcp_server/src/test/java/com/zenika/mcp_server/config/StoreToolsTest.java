package com.zenika.mcp_server.config;

import com.zenika.mcp_server.model.OrderRequest;
import com.zenika.mcp_server.model.OrderResponse;
import com.zenika.mcp_server.model.ProductResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class StoreToolsTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreTools storeTools;

    private ProductResponse laptopPro;
    private ProductResponse laptopAir;
    private ProductResponseList allProducts;
    private OrderRequest validOrderRequest;
    private OrderResponse confirmedOrderResponse;
    private OrderResponse dispatchedOrderResponse;

    @BeforeEach
    void setUp() {
        // Product Responses
        laptopPro = new ProductResponse(1, "Laptop Pro", 1200.00, 50, "A powerful laptop for professionals.");
        laptopAir = new ProductResponse(2, "Laptop Air", 999.99, 30, "Lightweight and portable.");
        allProducts = new ProductResponseList(List.of(laptopPro, laptopAir));

        // Order request & response
        validOrderRequest = new OrderRequest("1", 2, 1, 101);
        confirmedOrderResponse = new OrderResponse("999", "CONFIRMED");
        dispatchedOrderResponse = new OrderResponse("123", "DISPATCHED");
    }

    @Test
    void testGetAllItems_ShouldDelegateToStoreService() {
        when(storeService.getAllItems()).thenReturn(new ProductResponseList(List.of(laptopPro)));

        ProductResponseList result = storeTools.getAllItems();

        assertThat(result.products()).hasSize(1);
        assertThat(result.products().get(0).name()).isEqualTo("Laptop Pro");
        verify(storeService).getAllItems();
    }

    @Test
    void testSearchProducts_ShouldDelegateToStoreService_WithMultipleResults() {
        when(storeService.searchProducts("Laptop")).thenReturn(allProducts);

        ProductResponseList result = storeTools.searchProducts("Laptop");

        assertThat(result.products()).hasSize(2);
        assertThat(result.products().get(0).name()).isEqualTo("Laptop Pro");
        assertThat(result.products().get(1).name()).isEqualTo("Laptop Air");

        verify(storeService).searchProducts("Laptop");
    }

    @Test
    void testPlaceOrder_ShouldDelegateToStoreService() {
        when(storeService.placeOrder(validOrderRequest)).thenReturn(confirmedOrderResponse);

        OrderResponse result = storeTools.placeOrder(validOrderRequest);

        assertThat(result).isSameAs(confirmedOrderResponse);
        assertThat(result.orderId()).isEqualTo("999");
        verify(storeService).placeOrder(validOrderRequest);
    }

    @Test
    void testGetOrderStatus_ShouldReturnOrderResponse_WhenOrderExists() {
        when(storeService.getOrderStatus("123")).thenReturn(Optional.of(dispatchedOrderResponse));

        Object result = storeTools.getOrderStatus("123");

        assertThat(result).isInstanceOf(OrderResponse.class);
        OrderResponse response = (OrderResponse) result;
        assertThat(response.status()).isEqualTo("DISPATCHED");

        verify(storeService).getOrderStatus("123");
    }

    @Test
    void testGetOrderStatus_ShouldReturnFallback_WhenOrderNotFound() {
        String missingOrderId = "999";
        when(storeService.getOrderStatus(missingOrderId)).thenReturn(Optional.empty());

        Object result = storeTools.getOrderStatus(missingOrderId);

        assertThat(result).isInstanceOf(OrderResponse.class);
        OrderResponse fallback = (OrderResponse) result;
        assertThat(fallback.orderId()).isEqualTo("999");
        assertThat(fallback.status()).contains("not found");

        verify(storeService).getOrderStatus(missingOrderId);
    }
}
