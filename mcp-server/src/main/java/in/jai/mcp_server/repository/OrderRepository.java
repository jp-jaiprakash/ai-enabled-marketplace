package in.jai.mcp_server.repository;

import in.jai.mcp_server.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {}