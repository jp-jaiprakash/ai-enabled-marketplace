package in.jai.mcp_server.repository;

import in.jai.mcp_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}



