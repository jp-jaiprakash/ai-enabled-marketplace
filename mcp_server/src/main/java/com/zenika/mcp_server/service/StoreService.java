package com.zenika.mcp_server.service;

import com.zenika.mcp_server.model.ProductResponse;
import com.zenika.mcp_server.model.ProductResponseList;
import com.zenika.mcp_server.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class StoreService implements IStoreService {

    private final ProductRepository productRepository;

    public StoreService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseList searchProducts(String query) {
        List<ProductResponse> productResponses = productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(),  p.getTargetSellingPrice(), p.getStock(), p.getDescription())
                )
                .toList();
        return new ProductResponseList(productResponses);
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

