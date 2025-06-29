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
             targetSellingPrice field used to determine the minimum and target price for the negotiation. If user starts negotiating then dont show minimumSellingPrice at once.
             Negotiation should be done in a friendly manner, and the agent should try to charm the client.
            """)
    ChatClient sellerAgentChatClient(ChatClient.Builder chatClientBuilder, PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {


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

/**
 * The issue in v1 was not memory issue, but rather the fact that the it's a logical flaw in how the conversation is handed off between your different AI agents.
 *
 * The "Why": The Agent Switch
 * The problem happens right here:
 *
 * User: "last name is prakash and customer number 5"
 *
 * AI: "Hello Jai Prakash! Thank you... How can I assist you today with your account or user name changes?"
 *
 * This response strongly suggests that your routerChatClient interpreted the mention of "last name" and "customer number" as a user management task and delegated the conversation to the userManagementChatClient. At this point, this agent has your details.
 *
 * User: "no i want to see all the available items"
 *
 * AI: "Hello Jai Prakash! Before I list all the available items for you, could you please confirm your last name and customer number?"
 *
 * When you changed your intent, the routerChatClient correctly re-evaluated the new prompt. It saw "available items" and handed the conversation off to a different agent: the sellerAgentChatClient.
 *
 * This is the root cause. Even though both agents share the same underlying ChatMemory (which is why it remembers your first name, "Jai Prakash"), the sellerAgentChatClient is essentially "waking up" for the first time. Its primary instruction, coming from GENERAL_MARKET_PLACE_PROMPT, is to always ask for the user's details.
 *
 * Think of it like talking to two different customer service representatives in a call center. You give your details to the first person (User Management). When you ask about products, they transfer you to a new person in the Sales department. That new representative, following their own standard protocol, starts by asking for your details again to verify who they're talking to.

 * Solution: Make the Prompt More Intelligent
 * The simplest and most effective solution is to change the core instruction. Instead of telling the AI to blindly ask for information, you should instruct it to ensure it has the information, checking the history first.

 */
