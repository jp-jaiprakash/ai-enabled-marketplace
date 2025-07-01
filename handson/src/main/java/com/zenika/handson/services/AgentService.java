package com.zenika.handson.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.zenika.handson.constant.PromptContants.ROUTER_PROMPT;

@Service
public class AgentService {

    private final ChatClient routerChatClient;
    private final PromptBeanService promptBeanService;
    private final Map<String, ChatClient> chatClientMap;

    /**
     * Constructor for AgentService.
     *
     * @param delegateChatClient A map of chat clients where the key is the client name and the value is the ChatClient instance.
     * @param promptBeanService  Service to handle bean definitions and descriptions.
     * @param chatClientBuilder  Builder to create ChatClient instances.
     */
    public AgentService(Map<String, ChatClient> delegateChatClient,
                        PromptBeanService promptBeanService,
                        ChatClient.Builder chatClientBuilder) {

        this.chatClientMap = delegateChatClient;
        this.promptBeanService = promptBeanService;

        // Build the system prompt for the router chat client
        String systemPrompt = ROUTER_PROMPT + this.promptBeanService.getAllBeansDefinitionDescriptions(this.chatClientMap.keySet());

        // Create the router chat client with the system prompt
        this.routerChatClient = chatClientBuilder.defaultSystem(systemPrompt)
                .build();
    }

    /**
     * This method routes the user inquiry to the appropriate chatClient/Agent based on the system prompt.
     * @param inquiry The inquiry made by the user
     * @return The name of the resolved chatClient/Agent that should handle the inquiry
     */
    public String routeInquiry(String inquiry) {
        // Use the router chat client to determine the appropriate chat client for the inquiry
        // Blocking call to get the response
        return this.routerChatClient
                .prompt()
                .user(inquiry)
                .call() // Blocking call to get the response
                .content();
    }

    /**
     * Retrieves the ChatClient based on the client name.
     *
     * @param clientName The name of the chat client to retrieve.
     * @return The ChatClient associated with the given name.
     * @throws NoSuchElementException if no chat client is found for the given name.
     */
    public ChatClient getChatClient(String clientName) {
        var chatClient =  chatClientMap.get(clientName);
        if (chatClient == null) {
            throw new NoSuchElementException("No chat client found for name: " + clientName);
        }
        return chatClient;
    }
}
