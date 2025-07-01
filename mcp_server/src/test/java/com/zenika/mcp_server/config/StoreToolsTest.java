package com.zenika.mcp_server.config;

import com.zenika.mcp_server.model.ProductResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreToolsTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreTools storeTools;

    @Test
    void testGetAllItems_ShouldDelegateToStoreService() {
        // Arrange: Create the expected response from the service.
        ProductResponse productDto = new ProductResponse(1, "Test Item", 99.99, 10, "A test item.");
        ProductResponseList expectedResponse = new ProductResponseList(List.of(productDto));

        // Configure the mock service to return our expected response.
        when(storeService.getAllItems()).thenReturn(expectedResponse);

        // Act: Call the tool's method.
        ProductResponseList actualResponse = storeTools.getAllItems();

        // Assert:
        // 1. Verify that the response from the tool is the same one we configured the service to return.
        assertThat(actualResponse).isSameAs(expectedResponse);
        assertThat(actualResponse.products()).hasSize(1);
        assertThat(actualResponse.products().get(0).name()).isEqualTo("Test Item");

        // 2. (Optional but good practice) Verify that the getAllItems method on the service was actually called once.
        verify(storeService).getAllItems();
    }
}