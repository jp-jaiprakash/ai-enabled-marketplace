package com.zenika.handson.agents.termcondition;

import com.zenika.handson.services.PromptBeanService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.zenika.handson.constant.PromptContants.GENERAL_MARKET_PLACE_PROMPT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TermAndCondAgentTests {

	/*
	 * Import with @Autowired the ChatClient bean for the user agent
	 */
	@Autowired
	ChatClient termAndConditionChatClient;
	@Autowired
	PromptBeanService promptBeanService;

	private static final Logger logger = LoggerFactory.getLogger(TermAndCondAgentTests.class);

	@Test
	void getTheMinimumAgeInTermAndCond() {

		String userPrompt = GENERAL_MARKET_PLACE_PROMPT + this.promptBeanService.getBeanDefinitionDescription("termAndConditionChatClient");
		String response = termAndConditionChatClient.prompt()
				.system(userPrompt)
				.user("what the minimum age to access the platform? reply with the number only")
				.call()
				.content();
		logger.info(response);
		assertThat(response).isEqualTo("18");
	}



}
