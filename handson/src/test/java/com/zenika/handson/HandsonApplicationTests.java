package com.zenika.handson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class HandsonApplicationTests {

	@Value("${spring.ai.openai.api-key}")
	private String apiKey;

	@Test
	void contextLoads() {
		assertThat(apiKey).isNotEmpty();
	}


}
