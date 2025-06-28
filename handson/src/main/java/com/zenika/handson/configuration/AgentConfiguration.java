package com.zenika.handson.configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;

@Configuration
public class AgentConfiguration {

    @Bean
    PromptChatMemoryAdvisor defaultChatMemoryAdvisor(ChatMemory chatMemory) {
        // Create a PromptChatMemoryAdvisor instance using the ChatMemory
        return PromptChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    @Description("""
            This Chat should be use to handle inquiries about listing products,
             searching products, negotiating prices, reply to the Platform Terms and Conditions
             and settling the deal at a specific price with the client.
             During the negotiation this Chat should try to sell the product at the higher price possible
             by using any mean to charm the client. As a price reference, each product will have a minimumSellingPrice and a
             targetSellingPrice field used to determine the minimum and target price for the negotiation. Do not communicate the minimumSellingPrice
             to the client.
            """)
    ChatClient sellerAgentChatClient(ChatClient.Builder chatClientBuilder, PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {

        // --- REFINED LOGIC ---
        // Combine the general prompt with the specific description here.
        String systemPrompt = GENERAL_MARKET_PLACE_PROMPT +
                """
                 You are now in 'Seller Mode'. Your goal is to handle inquiries about listing products,
                 searching products, and negotiating prices. Try to sell the product at the higher price possible.
                 As a price reference, each product will have a minimumSellingPrice and a targetSellingPrice.
                 Do not communicate the minimumSellingPrice to the client.
                """;

        return chatClientBuilder
                .defaultSystem(systemPrompt) // Set the full default system prompt
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }

    @Bean
    @Description("""
            This Chat should be use to handle orders management only after the deal is settled at a agreed price for a product.
            This Chat should be able to handle inquiries about order status, shipping details, and any issues related to the order.
            This Chat will also be in charge of adding the order to the database or updating the order.
            """)
    ChatClient orderManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {
        String systemPrompt = GENERAL_MARKET_PLACE_PROMPT +
                """
                 You are now in 'Order Management Mode'. Handle inquiries about order status,
                 shipping details, and related issues. You can add or update orders in the database.
                """;
        return chatClientBuilder
                .defaultSystem(systemPrompt)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }

    @Bean
    @Description("""
            This Chat should be use to handle User management.
            This Chat should be able to handle inquiries about creating a new user, updating an existing user.
            This Chat will also be in charge of adding the new user to the database or updating the user information.
            """)
    ChatClient userManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {

        String systemPrompt = GENERAL_MARKET_PLACE_PROMPT +
                """
                 You are now in 'User Management Mode'. Handle inquiries about user name changes
                 . You can add or update users in the database.
                """;

        return chatClientBuilder
                .defaultSystem(systemPrompt)
                .defaultTools()
                .defaultToolCallbacks(toolCallbackProvider )
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }


}
