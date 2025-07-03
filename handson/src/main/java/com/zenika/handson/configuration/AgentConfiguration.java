package com.zenika.handson.configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;

@Configuration
public class AgentConfiguration {

    /**
     * This bean provides a default ChatMemoryAdvisor that will be used by all the ChatClients.
     * It is used to store the conversation history and provide context to the chat clients.
     *
     * @param chatMemory The ChatMemory instance to be used by the advisor.
     * @return A PromptChatMemoryAdvisor instance configured with the provided ChatMemory.
     */
    @Bean
    PromptChatMemoryAdvisor defaultChatMemoryAdvisor(ChatMemory chatMemory) {
        return PromptChatMemoryAdvisor.builder(chatMemory).order(1).build();
    }

    /**
     * This bean provides a default ToolCallbackProvider that will be used by all the ChatClients.
     * It is used to provide the tools that the chat clients can use to answer the user inquiries.
     *
     * @return A ToolCallbackProvider instance configured with the default tools.
     */
    @Bean
    @Description("""
        This Chat should be use to handle inquiries about listing products,
         searching products, negotiating prices
         and settling the deal at a specific price with the client.
         Follow this specific negotiation strategy:
                 1.  **Starting Offer:** Always start your negotiation with an offer that is close to the 'targetSellingPrice'.
                 2.  **Handle Counter-Offers:** When a user makes a counter-offer, do not immediately accept it or jump to your lowest price.
                 3.  **Gradual Concessions:** Instead, aim for at least **two to three rounds of negotiation**. For each round, make a small concession, moving your offer slightly lower towards the user's price.
                 4.  **Charm and Justify:** With each new counter-offer you make, you must 'charm the client' by justifying the price. Highlight a specific, valuable feature of the product. For example, "I can't quite do $850, but I can meet you at $920. At that price, you're getting the state-of-the-art processor which makes it a fantastic deal."
                 5.  **Know Your Limit:** Your absolute floor is the 'minimumSellingPrice'. Under no circumstances should you offer or accept a price below this value. Never reveal the 'minimumSellingPrice' to the user.
                 6.  **Negotiation should be done in a friendly manner, and the agent should try to charm the client.
        """)
    ChatClient sellerAgentChatClient(ChatClient.Builder chatClientBuilder, PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {
        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }
    /**
     * This bean provides a ChatClient that will be used to handle inquiries about orders management.
     * It is used to handle inquiries about order status, shipping details, and any issues related to the order.
     * It will also be in charge of adding the order to the database or updating the order.
     *
     * @param chatClientBuilder The ChatClient.Builder instance to build the ChatClient.
     * @param defaultChatMemoryAdvisor The default ChatMemoryAdvisor to be used by the ChatClient.
     * @param toolCallbackProvider The ToolCallbackProvider to provide tools for the ChatClient.
     * @return A ChatClient instance configured for order management.
     */
    @Bean
    @Description("""
            This Chat should be use to handle orders management only after the deal is settled at a agreed price for a product.
            This Chat should be able to handle inquiries about order status, shipping details, and any issues related to the order.
            This Chat will also be in charge of adding the order to the database.
            """)
    ChatClient orderManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {
        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }

    /**
     * This bean provides a ChatClient that will be used to handle inquiries about user management.
     * It is used to handle inquiries about creating a new user, updating an existing user.
     * It will also be in charge of adding the new user to the database or updating the user information.
     *
     * @param chatClientBuilder The ChatClient.Builder instance to build the ChatClient.
     * @param defaultChatMemoryAdvisor The default ChatMemoryAdvisor to be used by the ChatClient.
     * @param toolCallbackProvider The ToolCallbackProvider to provide tools for the ChatClient.
     * @return A ChatClient instance configured for user management.
     */
    @Bean
    @Description("""
            This Chat should be use to handle User management.
            This Chat should be able to handle inquiries about creating a new user, updating an existing user.
            This Chat can add or update users in the database.Validate if user exists in database by given id
            """)
    ChatClient userManagementChatClient(ChatClient.Builder chatClientBuilder,  PromptChatMemoryAdvisor defaultChatMemoryAdvisor, ToolCallbackProvider toolCallbackProvider ) {
        // --- USER MANAGEMENT PROMPT ---
        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultToolCallbacks(toolCallbackProvider )
                .defaultAdvisors(defaultChatMemoryAdvisor)
                .build();
    }
    /**
     * This bean provides a ChatClient that will be used to handle inquiries about the terms and conditions of the marketplace.
     * It is used to handle inquiries about the marketplace policies, user agreements, and any legal information.
     *
     * @param chatClientBuilder The ChatClient.Builder instance to build the ChatClient.
     * @param defaultChatMemoryAdvisor The default ChatMemoryAdvisor to be used by the ChatClient.
     * @param advisor The QuestionAnswerAdvisor to provide answers to questions related to terms and conditions.
     * @return A ChatClient instance configured for terms and conditions inquiries.
     */
    @Bean
    @Description("This Chat should be use answer all the term and conditions related inquiries.")
    ChatClient termAndConditionChatClient(ChatClient.Builder chatClientBuilder, PromptChatMemoryAdvisor defaultChatMemoryAdvisor, QuestionAnswerAdvisor advisor ) {
        return chatClientBuilder
                .defaultSystem(GENERAL_MARKET_PLACE_PROMPT)
                .defaultTools()
                .defaultAdvisors(defaultChatMemoryAdvisor, advisor)
                .build();
    }


}
