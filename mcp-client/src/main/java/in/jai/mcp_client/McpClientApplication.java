package in.jai.mcp_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = {
//		org.springframework.ai.autoconfigure.mcp.client.SseHttpClientTransportAutoConfiguration.class
//})
public class McpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpClientApplication.class, args);
	}

}
