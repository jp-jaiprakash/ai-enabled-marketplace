package com.zenika.mcp_server.service;

import com.zenika.mcp_server.model.UserResponse;
import com.zenika.mcp_server.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * === WORKSHOP TASK ===
 *
 * This service handles user validation.
 *
 * TODO:
 * - Inject the `UserRepository` using constructor injection.
 * - Implement a method to validate the existence of a user by ID.
 * - If the user exists, return a `UserResponse`.
 * - If not found, throw `IllegalArgumentException("User not found")`.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // TODO: Inject UserRepository via constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * === WORKSHOP TASK ===
     *
     * Validate whether a user exists for the given user ID.
     *
     * TODO:
     * 1. Use `userRepository.findById(userId)` to retrieve the user.
     * 2. If present, map to `UserResponse` and return.
     * 3. If not present, throw `IllegalArgumentException` with a helpful message.
     */
    public UserResponse validateUser(int userId) {
        // TODO: Retrieve user from repository

        // TODO: If user is not found, throw IllegalArgumentException

        // TODO: Return UserResponse with ID, name, and empty string for extra field
        return null; // Placeholder
    }
}
