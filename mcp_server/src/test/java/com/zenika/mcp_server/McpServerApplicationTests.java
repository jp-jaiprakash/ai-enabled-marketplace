package com.zenika.mcp_server;

import com.zenika.mcp_server.config.StoreTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class McpServerApplicationTests {

	@Autowired
	private ToolCallbackProvider toolCallbackProvider;

	@Autowired
	private StoreTools storeTools;

	/**
	 * === WORKSHOP TASK ===
	 *
	 * This test ensures that the Tool infrastructure has been properly initialized.
	 *
	 * TODO:
	 * 1. Assert that the `toolCallbackProvider` bean is loaded and is not null.
	 * 2. Assert that it's an instance of `MethodToolCallbackProvider`.
	 * 3. Optionally: Verify how many tool methods were registered.
	 *    (Hint: use `getToolCallbacks().length`)
	 */
	@Test
	void testToolsBean_ShouldBeCreated() {
		// Step 1
		assertThat(toolCallbackProvider).isNotNull();

		// Step 2
		assertThat(toolCallbackProvider).isInstanceOf(MethodToolCallbackProvider.class);

		// Step 3
		MethodToolCallbackProvider provider = (MethodToolCallbackProvider) toolCallbackProvider;
		assertThat(provider.getToolCallbacks().length).isGreaterThan(0); // ✅ You can hardcode `== 4` if expected
	}
}
