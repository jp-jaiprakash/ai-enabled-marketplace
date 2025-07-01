package com.zenika.handson.controllers;

import com.zenika.handson.services.AgentService;
import com.zenika.handson.services.PromptBeanService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;


import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;


record ChatRequest(String message, String user) {}
record ChatResponse(String response) {}

@RestController
@RequestMapping("/api/marketplace")
public class AiMarketPlaceController {

    private final AgentService agentService;
    private final PromptBeanService promptBeanService;
    /**
     * Constructor for AiMarketPlaceController.
     *
     * @param agentService       Service to handle agent routing and chat client management.
     * @param promptBeanService  Service to handle bean definitions and descriptions.
     */
    public AiMarketPlaceController(AgentService agentService, PromptBeanService promptBeanService) {
        this.agentService = agentService;
        this.promptBeanService = promptBeanService;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
    @PostMapping("/chat")
    public ChatResponse generate(@RequestBody ChatRequest chatRequest) {

        // Get the name of the Agent/ChatClient base on the user inquiry
        var resolvedChatClient = this.agentService.routeInquiry(chatRequest.message());

        String response =  this.agentService.getChatClient(resolvedChatClient)
                .prompt()
                .user(chatRequest.message())
                .system(GENERAL_MARKET_PLACE_PROMPT + this.promptBeanService.getBeanDefinitionDescription(resolvedChatClient))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatRequest.user()))
                .call()
                .content();

        return new ChatResponse(response);
    }

}
