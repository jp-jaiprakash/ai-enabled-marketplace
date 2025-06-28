package com.zenika.handson.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;


record ChatRequest(String message, String user) {}
record ChatResponse(String response) {}

@RestController
@RequestMapping("/api/marketplace")
public class AiMarketPlaceController {
    /**
     * This controller acts as a router for different chat clients based on the inquiry type.
     * It uses a system prompt to determine which chat client to delegate the request to.
     */
    private final DefaultListableBeanFactory beanFactory;
    private final ChatClient routerChatClient;
    private final Map<String, ChatClient> delegateChatClient;

    public AiMarketPlaceController(
            Map<String, ChatClient> chatClients,
            ChatClient.Builder chatClientBuilder,
            DefaultListableBeanFactory beanFactory
    ) {
        this.beanFactory = beanFactory;
        delegateChatClient = chatClients;

        // Build the system prompt for the router chat client
        StringBuilder systemPrompt = new StringBuilder("""
                You are a router. When a request comes in, inspect the request and determine which of the following categories best matches the nature of the request and then return the category, and only the category of the best match. Here is a description of each category and an explanation of why you might choose that category.
                """);
        // Iterate over the chat clients and append their descriptions to the system prompt
        for (var beanName : chatClients.keySet()) {

            var beanDefinitionDescription = getBeanDefinitionDescription(beanName);
            systemPrompt.append(String.format("\n\n%s: %s\n\n", beanName, beanDefinitionDescription));
        }

        this.routerChatClient = chatClientBuilder.defaultSystem(systemPrompt.toString())
                .build();

    }


    @CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
    @PostMapping("/chat")
    public ChatResponse generate(@RequestBody ChatRequest chatRequest) {
        var resolvedChatClient = this.routerChatClient
                .prompt()
                .user(chatRequest.message())
                .call()
                .content();

        System.out.println(resolvedChatClient);

        String response =  delegateChatClient
                .get(resolvedChatClient)
                .prompt()
                .user(chatRequest.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatRequest.user()))
                .call()
                .content();

        return new ChatResponse(response);
    }

    /**
     * Retrieves the description of a bean definition by its name.
     *
     * @param beanName the name of the bean
     * @return the description of the bean definition, or a default message if no description is available
     */
    private String getBeanDefinitionDescription(String beanName) {
        var benDefinition = beanFactory.getBeanDefinition(beanName);
        var beanDefinitionDescription = benDefinition.getDescription();
        return beanDefinitionDescription != null ? beanDefinitionDescription : "No description available";
    }
}
