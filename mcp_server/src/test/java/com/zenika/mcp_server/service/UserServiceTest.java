package com.zenika.mcp_server.service;

import com.zenika.mcp_server.entity.User;
import com.zenika.mcp_server.model.UserResponse;
import com.zenika.mcp_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alice");
        user.setEmail("alice@example.com");
    }

    @Test
    void testValidateUser_ShouldReturnUserResponse_WhenUserExists() {
        // TODO: Mock userRepository.findById to return the sample user
        // TODO: Call userService.validateUser with user ID 1
        // TODO: Assert that the returned UserResponse has correct ID and name
    }

    @Test
    void testValidateUser_ShouldThrowException_WhenUserNotFound() {
        // TODO: Mock userRepository.findById to return Optional.empty()
        // TODO: Assert that calling userService.validateUser throws IllegalArgumentException
    }
}  
