package com.zenika.handson.agents.userManagement;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserAgentTests {

	/*
	 * Import with @Autowired the ChatClient bean for the seller agent
	 */
	@Autowired
	ChatClient userManagementChatClient;

	private static final Logger logger = LoggerFactory.getLogger(UserAgentTests.class);

	@Test
	void getExistingUserById() {

		String response = userManagementChatClient.prompt()
				.user("can you return the name and only the name of the user with id 1")
				.call()
				.content();
		logger.info(response);
		assertThat(response).isNotEmpty();
	}

	@Test
	void createNewUSer() {
		String response = userManagementChatClient.prompt()
				.user("Can you create a new user with name John Doe and email lol@yopmail.com and address 123 Main St," +
						" Cityville, Countryland and return only the user id. If you don't have the ability reply with empty string")
				.call()
				.content();
		logger.info(response);
		assertThat(response).isNotEmpty();
	}

	@Test
	void updateUSer() {
		String response = userManagementChatClient.prompt()
				.user("Can you  update the user with id 1 to have name Jane Doe and return the name of the user if the update was successful, otherwise return an empty string")
				.call()
				.content();
		logger.info(response);
		assertThat(response).isNotEmpty();
	}



}
