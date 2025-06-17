package com.zenika.handson.configuration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;

@Configuration
public class AgentConfiguration {
//    @Bean
//    McpSyncClient mcpSyncClient(@Value("${server.mcp.url}") String mcpUrlServer){
//        // Create a WebClient instance
//        HttpClientSseClientTransport transport = HttpClientSseClientTransport.builder(mcpUrlServer).build();
//        // Create a McpClient instance using the WebClient
//        var mcpClient = McpClient.sync(transport).build();
//        // Initialize the McpClient
//        mcpClient.initialize();
//        return mcpClient;
//    }

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
             targetSellingPrice field used to determine the minimum and target price for the negotiation.
            """)
    ChatClient sellerAgentChatClient(ChatClient.Builder chatClientBuilder, PromptChatMemoryAdvisor defaultChatMemoryAdvisor) {

        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultTools()
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }

    @Bean
    @Description("""
            This Chat should be use to handle orders management only after the deal is settled at a agreed price for a product.
            This Chat should be able to handle inquiries about order status, shipping details, and any issues related to the order.
            This Chat will also be in charge of adding the order to the database or updating the order.
            """)
    ChatClient orderManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor) {

        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultTools()
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }

    @Bean
    @Description("""
            This Chat should be use to handle User management.
            This Chat should be able to handle inquiries about creating a new user, updating an existing user.
            This Chat will also be in charge of adding the new user to the database or updating the user information.
            """)
    ChatClient userManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor) {

        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultTools()
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }


}
