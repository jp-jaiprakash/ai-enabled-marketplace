package com.zenika.mcp_server;

import com.zenika.mcp_server.config.StoreTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

	/**
	 * === WORKSHOP TASK ===
	 *
	 * The AI model needs to know which Java functions it's allowed to call.
	 * Your task is to create a Spring Bean that provides these functions as "Tools".
	 *
	 * 1. Use the `MethodToolCallbackProvider.builder()` to start the configuration.
	 * 2. Register the `sellerAccountTools` object, which contains the functions we want to expose.
	 * (Hint: Look for a method on the builder called `toolObjects`).
	 * 3. Build the provider to complete the bean creation.
	 */
	@Bean
	public ToolCallbackProvider tools(StoreTools sellerAccountTools) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(sellerAccountTools)
				.build();
	}
}
