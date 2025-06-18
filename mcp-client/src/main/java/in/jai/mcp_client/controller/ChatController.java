package in.jai.mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

record ChatRequest(String message) {}
record ChatResponse(String response) {}

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final Map<String, PromptChatMemoryAdvisor> memory = new ConcurrentHashMap<>();

    private final ToolCallbackProvider toolCallbackProvider;

    public ChatController(ChatClient.Builder chat, ToolCallbackProvider toolCallbackProvider  ) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.chatClient = chat
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }



    @CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
    @PostMapping("/api/chat")
    public ChatResponse generate(@RequestBody ChatRequest chatRequest) {
        String user = "jai";

        var system = """
                You are an AI powered assistant to help people in browsing, buying and placing an order from a website \s
                named saffronweaves with locations in SIngapore. Information about clothes will be provided below. Be \s
                polite if no information is available.
                """;
// Create or retrieve the chat memory for the user
//        PromptChatMemoryAdvisor chatMemory = memory.computeIfAbsent(user, k ->
//            PromptChatMemoryAdvisor.builder(new InMemoryChatMemory()));

        String response = chatClient.prompt()
                // ** THE FIX IS HERE **
                // We pass the entire component containing @Tool-annotated methods.
//                .toolCallbacks(toolCallbackProvider)
                .user(chatRequest.message())
                .system(system)
                .call()
                .content();

        return new ChatResponse(response);
    }

}

//https://medium.com/@gareth.hallberg_55290/part-4-remembering-the-conversation-implementing-chat-history-with-spring-ai-676d6c566ca3