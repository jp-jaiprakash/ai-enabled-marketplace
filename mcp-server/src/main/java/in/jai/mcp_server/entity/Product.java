package in.jai.mcp_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Double minimumSellingPrice;

    private Double targetSellingPrice;

    private Integer stock;

    private Integer estimateDeliveryDays;

    private LocalDateTime createdAt = LocalDateTime.now();
}
