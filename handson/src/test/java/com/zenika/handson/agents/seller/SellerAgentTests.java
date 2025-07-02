package com.zenika.handson.agents.seller;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SellerAgentTests {

	/*
	 * Import with @Autowired the ChatClient bean for the seller agent
	 */
	@Autowired
	ChatClient sellerAgentChatClient;

	private static final Logger logger = LoggerFactory.getLogger(SellerAgentTests.class);

	@Test
	void sellerAgentCanCommunicate() {
		/*
		  Use the sellerAgentChatClient to send a message to the agent
		 */
		String response = sellerAgentChatClient.prompt()
				.user("Hello, I am looking for a new laptop.")
				.call()
				.content();
		logger.info(response);
		assertThat(response).isNotEmpty();
	}

	@Test
	void sellerAgentChatMemory() {
		/*
		  set your name in the string variable `yourName`
		 */
		String yourName = "John Doe"; // Replace with your name
		var message = "My name is " + yourName;

		String responseOne = sellerAgentChatClient.prompt()
				.user(message)
				.call()
				.content();
		logger.info(responseOne);

		String responseTwo = sellerAgentChatClient.prompt()
				.user("What is my name?")
				.call()
				.content();
		logger.info(responseTwo);

        assert responseTwo != null;
        assertThat(responseTwo.contains(yourName)).isTrue();
	}

	@Test
	void getAllProduct() {
		/*
		  set your name in the string variable `yourName`
		 */
		String yourName = "John Doe"; // Replace with your name
		var message = "My name is " + yourName + " my customer number is 101. Can you show me all the products? and return the response in JSON format, only the JSON, nothing else.";

		String responseOne = sellerAgentChatClient.prompt()
				.user(message)
				.call()
				.content();
		logger.info(responseOne);
		String expectedResponse = """
				{"products":[{"id":1,"name":"Laptop","price":999.99,"stock":10,"description":"High-performance laptop"},{"id":2,"name":"Headphones","price":79.99,"stock":25,"description":"Noise-cancelling headphones"},{"id":3,"name":"Smartphone","price":699.99,"stock":15,"description":"Flagship smartphone model"},{"id":4,"name":"Backpack","price":35.0,"stock":50,"description":"Waterproof backpack"},{"id":5,"name":"Monitor","price":249.99,"stock":8,"description":"27 inch 4K monitor"}]}""";
		assertThat(responseOne).isEqualTo(expectedResponse);
	}


}
