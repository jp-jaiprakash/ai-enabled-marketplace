package com.zenika.handson.agents;

import com.zenika.handson.services.AgentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AgentServiceTests {

	/*
	 * Import with @Autowired the AgentService bean
	 */
	@Autowired
	AgentService agentService;

	private static final Logger logger = LoggerFactory.getLogger(AgentServiceTests.class);

	@Test
	void getTheSellerAgentForProductInquiry() {
		/*
		 Get the Seller Agent when the user request is about a product inquiry.
		 */
		String response = agentService.routeInquiry("Can you list all the products ?");
		logger.info("Response from AgentService: {}", response);
		assertThat(response).isEqualTo("sellerAgentChatClient");
	}

	@Test
	void sellerAgentIsPartOfAgentMap() {
		/*
		 Get the Seller Agent when the user request is about a product inquiry.
		 */
		ChatClient response = agentService.getChatClient("sellerAgentChatClient");
		assertThat(response).isNotNull();
	}



}
